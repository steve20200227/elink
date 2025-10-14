package cn.snowsoft.iot.module.cps.controller.admin.equipment;


import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPage;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.dto.EquipmentBatchAddDTO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.ekuiperClient.MonitorClient;
import cn.snowsoft.iot.module.cps.service.cpsRule.CpsRuleService;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import cn.snowsoft.iot.module.cps.service.product.ProductService;
import cn.snowsoft.iot.module.cps.utils.StringToList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Tag(name = "管理后台 - 设备管理")
@RestController
@RequestMapping("/cps/equipment")
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;
    @Resource
    private CpsRuleService cpsRuleService;
    @Resource
    private ProductService productService;
    @Resource
    private RedisCacheManager redisCacheManager;

    /**
     * 新增或修改
     */
    @PostMapping("/addAuthentication")
    @ApiOperationSupport(order = 3)
    public CommonResult addAuthentication(@RequestBody EquipmentPage equipment) {
        //添加账号
        return equipmentService.addAuthentication(equipment);
    }

    /**
     * 查询自定义分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 1)
    public CommonResult<IPage<EquipmentPage>> page(EquipmentPageVO equipment) {
        IPage<EquipmentPage> pages = equipmentService.selectEquipmentPage(equipment);
        return CommonResult.success(pages);
    }

    /**
     * 查询指定状态的设备列表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<EquipmentDO>> page(EquipmentDO equipment) {
        List<EquipmentDO> list = equipmentService.selectEquipmentList(equipment);
        return CommonResult.success(list);
    }

    /**
     * 批量启用
     */
    @PostMapping("/enableEquipment")
    @ApiOperationSupport(order = 5)
    public CommonResult<Boolean> enableEquipment(@RequestParam String ids) {
        return CommonResult.success(equipmentService.enableEquipment(StringToList.toLongList(ids)));
    }

    /**
     * 批量失效
     */
    @PostMapping("/disenableEquipment")
    @ApiOperationSupport(order = 12)
    public CommonResult<Boolean> disenableEquipment(@RequestParam String ids) {
        return CommonResult.success(equipmentService.disenableEquipment(StringToList.toLongList(ids)));
    }

    /**
     * 删除, 可删除的前提条件是，设备已失效
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 4)
    public CommonResult<Boolean> remove(@RequestBody EquipmentPage equipment) {
        return CommonResult.success(equipmentService.removeEquipment(equipment));
    }

    /**
     * 重启
     *
     * @return
     */
    @GetMapping("/reStart")
    @ApiOperationSupport(order = 15)
    public CommonResult reStart() {
        return CommonResult.success(equipmentService.restart());
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 3)
    public CommonResult submit(@RequestBody EquipmentDO equipment) {
        return equipmentService.submit(equipment);
    }




    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    public CommonResult<EquipmentVO> detail(Long id) {
        EquipmentDO equipmentDO = equipmentService.getById(id);
        EquipmentVO equipmentVO = new EquipmentVO();
        BeanUtils.copyProperties(equipmentDO, equipmentVO);
        //从redis中获取当前设备的状态、网络位置、最后活跃时间
        String info = redisCacheManager.getCache("deviceStatus").get(equipmentVO.getEquipmentCode(), String.class);
        if (StringUtil.isNotBlank(info)) {
            String[] split = info.split("-");
            //设备状态
            equipmentVO.setStatus(Integer.parseInt(split[0]));
            //网络位置
            equipmentVO.setNetLocation(split[1]);
            //最后活跃时间
            equipmentVO.setLastActivated(new Date(Long.parseLong(split[2])));
        } else {
            equipmentVO.setStatus(0);
            equipmentVO.setLastActivated(null);
            equipmentVO.setNetLocation("");
        }

        if (StringUtil.isNotBlank(equipmentDO.getParentEquipment())) {
            equipmentVO.setParentEquipmentDO(equipmentService.getOne(new LambdaQueryWrapper<EquipmentDO>().eq(EquipmentDO::getEquipmentCode, equipmentDO.getParentEquipment())));
        }
        //获取设备关联的规则
        List<CpsRule> cpsRuleList = cpsRuleService.lambdaQuery().eq(CpsRule::getRelevanceId, id).list();

        cpsRuleList.stream().forEach(e -> {
            if ("attribute".equals(e.getType())) {
                equipmentVO.setReportId(e.getId());
            }else if ("event".equals(e.getType())) {
                equipmentVO.setEventId(e.getId());
            } else if ("feature".equals(e.getType())) {
                equipmentVO.setDeliverAnId(e.getId());
            }
        });

        return CommonResult.success(equipmentVO);
    }

    /**
     * 设备数据监控接口，此接口会针对当前设备进行ekuiper规则创建，请勿在其他业务处使用
     */
    @GetMapping("/detailMonitor")
    @ApiOperationSupport(order = 1)
    public CommonResult<EquipmentDO> detailMonitor(Long id) {
        return CommonResult.success(equipmentService.detailMonitor(id));
    }


    /**
     * 根据产品编号查询设备
     *
     * @param equipment equipment实体
     * @return CommonResult<IPage < EquipmentDO>>
     */
    @GetMapping("/pageByProductCode")
    @ApiOperationSupport(order = 1)
    public CommonResult pageByProductCode(EquipmentPageVO equipment) {
        List<EquipmentPage> equipmentDOS = equipmentService.selectEquipmentPageByProductCode(equipment);
        return CommonResult.success(equipmentDOS);
    }

    @GetMapping("/cancelPassage")
    @ApiOperationSupport(order = 1)
    public CommonResult cancelPassage(String id) {
        return equipmentService.cancelPassage(id);
    }

    @GetMapping("/getEquipmentOptions")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<EquipmentDO>> getEquipmentOptions(@RequestParam String productCode) {
        ProductDO productDO = productService.getOne(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getProductCode, productCode));
        return CommonResult.success(equipmentService.getBaseMapper().selectList(new LambdaQueryWrapper<EquipmentDO>()
                .eq(EquipmentDO::getEquipmentType, "网关设备")
                .eq(EquipmentDO::getProductCode, productDO.getParentProduct()))); // 关联的产品父网关产品
    }

    /**
     * 根据ids获取对应数据
     */
    @PostMapping("/getByIds")
    public CommonResult<List<EquipmentDO>> getByIds(@RequestParam("ids") String ids) {
        return CommonResult.success(equipmentService.getByIds(ids));
    }

    /**
     * 判断设备下是否有子设备
     * @return
     */
    @GetMapping("/getChildEquipment")
    public CommonResult getChildEquipment(String equipmentCode) {
        return equipmentService.getChildEquipment(equipmentCode);
    }


    @GetMapping("/selectRuleList")
    @ApiOperationSupport(order = 1)
    public CommonResult selectRuleList(EquipmentPageVO equipment) {
        IPage<EquipmentDO> equipmentList =  equipmentService.selectRuleList(equipment);
        return CommonResult.success(equipmentList);
    }

    /**
     * 批量新增
     */
    @PostMapping("/BatchAdd")
    @ApiOperationSupport(order = 16)
    public CommonResult batchAdd(@RequestBody EquipmentBatchAddDTO equipment) {
        return equipmentService.batchAdd(equipment);
    }

}
