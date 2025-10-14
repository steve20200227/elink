package cn.snowsoft.iot.module.cps.service.equipment;

import cn.hutool.core.util.ObjectUtil;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentPointPageVO;
import cn.snowsoft.iot.module.cps.controller.admin.equipment.vo.EquipmentQueryVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import cn.snowsoft.iot.module.cps.dal.mysql.equipment.EquipmentMapper;
import cn.snowsoft.iot.module.cps.dal.mysql.equipment.EquipmentPointMapper;
import cn.snowsoft.iot.module.cps.service.product.ProductPointService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentPointServiceImpl extends ServiceImpl<EquipmentPointMapper, EquipmentPointDO> implements EquipmentPointService {

    @Resource
    private ProductPointService productPointService;

    @Resource
    private EquipmentMapper equipmentMapper;

    @Resource
    private EquipmentService equipmentService;

    @Override
    public IPage<EquipmentPointDO> selectPointPage(EquipmentPointPageVO point) {
        IPage<EquipmentPointDO> page = new Page<>(point.getPageNo(), point.getPageSize());
        return baseMapper.selectPointPage(page, point);
    }

    @Override
    public Map getDeviceAttributeAndEvent(String equipmentCode) {
        EquipmentDO equipmentDO = ObjectUtil.defaultIfNull(equipmentService.lambdaQuery().eq(EquipmentDO::getEquipmentCode, equipmentCode).one(), new EquipmentDO());
        List<EquipmentPointDO> equipmentPointDOS = ObjectUtil.defaultIfNull(
                lambdaQuery().eq(EquipmentPointDO::getEquipmentId, equipmentDO.getId()).list(),
                new ArrayList<>());
        //按pointType分组
        Map<String, List<EquipmentPointDO>> pointTypeMap = equipmentPointDOS.stream().collect(Collectors.groupingBy(EquipmentPointDO::getPointType));
        return pointTypeMap;
    }

    @Override
    public Boolean deleteLogic(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public EquipmentPointDO submit(EquipmentPointDO point) {
        point.setIsCoverage(1); // 设置为1，表示为手动添加；
        List<EquipmentPointDO> pointList = checkCode(point);
        if (pointList.size() > 0) {
            throw new RuntimeException("点位编码已存在: " + point.getPointCode());
        }
        if (ObjectUtil.isEmpty(point.getSource())) {
            point.setSource("equipment");
        }
        saveOrUpdate(point);
        return point;
    }

    @Override
    public Boolean coveragePoint(Long equipmentId, Long productId) {
        // 删除之前的产品点位
        lambdaUpdate().eq(EquipmentPointDO::getEquipmentId, equipmentId)
                .eq(EquipmentPointDO::getSource, "product")
                .eq(EquipmentPointDO::getIsCoverage, 0) // 删除所有的覆盖带出点位
                .set(EquipmentPointDO::getDeleted, 1)
                .update();
        // 使用流和lambda表达式来简化循环和对象创建过程
        List<EquipmentPointDO> equipmentPointList = productPointService.lambdaQuery()
                .eq(ProductPointDO::getProductId, productId)
                .eq(ProductPointDO::getIsEnable, 1)
                .list()
                .stream()
                .map(e -> {
                    EquipmentPointDO equipmentPoint = new EquipmentPointDO();
                    BeanUtils.copyProperties(e, equipmentPoint);
                    equipmentPoint.setId(null);
                    equipmentPoint.setEquipmentId(equipmentId);
                    // 将常用属性置空，以免将产品点位的基础信息复制到设备点位
                    equipmentPoint.setPointCode(e.getPointCode()); // e.getPointCode()
                    equipmentPoint.setCreateUser(null);
                    equipmentPoint.setCreateDept(null);
                    equipmentPoint.setCreateTime(null);
                    equipmentPoint.setUpdateUser(null);
                    equipmentPoint.setUpdateTime(null);
                    equipmentPoint.setPointDict(e.getPointDict());
                    equipmentPoint.setIsCoverage(0); // 设置为覆盖带出点位
                    equipmentPoint.setSource("product");
                    return equipmentPoint;
                })
                .collect(Collectors.toList());
        saveBatch(equipmentPointList);
        return true;
    }

    @Override
    public void removePoint(Long equipmentId) {
        lambdaUpdate().eq(EquipmentPointDO::getEquipmentId, equipmentId).set(EquipmentPointDO::getDeleted, 1).update();
    }

    @Override
    public List<EquipmentPointDO> getByEquipmentId(Long equipmentId) {
        return baseMapper.selectList(new LambdaQueryWrapper<EquipmentPointDO>()
                .eq(EquipmentPointDO::getEquipmentId, equipmentId)
                .eq(EquipmentPointDO::getPointType, "pointFeature")
                .eq(EquipmentPointDO::getIsEnable, 1));
    }

    @Override
    public List<EquipmentPointDO> getByEquipmentIdsAndCode(EquipmentQueryVO equipmentQueryVO) {
        return lambdaQuery()
                .in(EquipmentPointDO::getEquipmentId, equipmentQueryVO.getEquipmentIds())
                .eq(EquipmentPointDO::getPointCode, equipmentQueryVO.getFeatureCode())
                .list();
    }

    @Override
    public List<EquipmentPointDO> getList(EquipmentPageVO point) {
        EquipmentDO one = equipmentService.lambdaQuery().eq(EquipmentDO::getEquipmentCode, point.getEquipmentCode()).one();
        List<EquipmentPointDO> list = lambdaQuery().eq(EquipmentPointDO::getEquipmentId, one.getId()).eq(EquipmentPointDO::getPointType, point.getPointType()).list();

        return list;
    }

    private List<EquipmentPointDO> checkCode(EquipmentPointDO point) {
        LambdaQueryWrapper<EquipmentPointDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (point.getPointCode().isEmpty()) {
            throw new RuntimeException("点位编码不能为空");
        }
        lambdaQueryWrapper.eq(EquipmentPointDO::getPointCode, point.getPointCode());
        // 只有在该设备下的点位编码不可重复-因为前端会将点位编码拼接设备编码
        lambdaQueryWrapper.eq(EquipmentPointDO::getEquipmentId, point.getEquipmentId());
        //id不为空 是修改 排除当前数据
        if (point.getId() != null) {
            lambdaQueryWrapper.ne(EquipmentPointDO::getId, point.getId());
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
