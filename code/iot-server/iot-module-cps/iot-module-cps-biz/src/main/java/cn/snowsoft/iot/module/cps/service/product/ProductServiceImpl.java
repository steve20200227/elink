package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.exception.ServiceException;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductAssociationEquipmentVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductStatusVO;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPage;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.dal.mysql.product.ProductMapper;
import cn.snowsoft.iot.module.cps.emqx.api.AuthenticationApi;
import cn.snowsoft.iot.module.cps.service.equipment.EquipmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {

    @Resource
    private ProductPointService pointService;

    @Resource
    private EquipmentService equipmentService;

    @Override
    public IPage<ProductPage> selectProductPage(ProductPageVO pageVO) {
        IPage<ProductPage> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize());
        return baseMapper.selectProductPage(page, pageVO);
    }

    @Override
    public CommonResult<Boolean> removeProduct(List<Long> ids) {
        // 判断产品下是否关联设备
        List<ProductPage> pages = this.isAssociationEquipment(ids);
        for (ProductPage page : pages) {
            if (page.getEquipmentCount() > 0) {
                return CommonResult.success(false);
            }
        }
        //到此说明无设备关联  直接删除账号密码
        for (ProductPage page : pages) {
            if (ObjectUtils.isNotEmpty(page.getUserAccount()) && ObjectUtils.isNotEmpty(page.getUserPassword())){
                try {
                    AuthenticationApi.removeAuthentication(page.getUserAccount());
                } catch (Exception e) {
                    log.error("删除账号密码失败:账号->{},密码->{}", page.getUserAccount(), page.getUserPassword());
                }
            }
        }
        // 删除相关点位信息
        for (Long id : ids) {
            pointService.removePoint(id);
        }
        removeBatchByIds(ids);
        return CommonResult.success(true);
    }

    @Override
    public List<ProductAssociationEquipmentVO> getEquipmentCount() {
        return baseMapper.getEquipmentCount();
    }

    @Override
    public List<ProductStatusVO> getIsEnableCount() {
        return baseMapper.getIsEnableCount();
    }

    @Override
    public List<ProductDO> getByProductSortId(Long id) {
        return baseMapper.selectList(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getProductSortId, id));
    }

    @Override
    public ProductDO getByCode(String productCode) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getProductCode, productCode));
    }


    public List<ProductPage> isAssociationEquipment(List<Long> ids) {
        return baseMapper.isAssociationEquipment(ids);
    }

    @Override
    public CommonResult addAuthentication(ProductPage productPage) {
        //如果为空  说明第一次创建 走新增 不为空则先判断设备是否有关联  没有就删除
        if (ObjectUtils.isNotEmpty(productPage.getUserAccount()) && ObjectUtils.isNotEmpty(productPage.getUserPassword())){
            //产品 关联设备且设备使用该产品账号时 不可删除账号
            LambdaQueryWrapper<EquipmentDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(EquipmentDO::getUserAccount, productPage.getUserAccount());
            List<EquipmentDO> list = equipmentService.list(lambdaQueryWrapper);
            if (list.size()>0){
                throw new ServiceException(500,"已有设备在使用此账号，不可重新生成！");
            }

            // 先删除原来的认证器
            try {
                ProductDO productDO = new ProductDO();
                productDO.setUserAccount(productPage.getUserAccount());
                productDO.setUserPassword(productPage.getUserPassword());
                AuthenticationApi.removeAuthentication(productDO.getUserAccount());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(500,"删除原账号失败");
            }
            try {
                productPage.setUserAccount("product_" + equipmentService.generateCode());
                productPage.setUserPassword("product_" + equipmentService.generateCode());
                AuthenticationApi.insertAuthentication(productPage.getUserAccount(), productPage.getUserPassword());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(500,"创建账号密码失败");
            }

        }else {
            try {
                productPage.setUserAccount("product_" + equipmentService.generateCode());
                productPage.setUserPassword("product_" + equipmentService.generateCode());
                AuthenticationApi.insertAuthentication(productPage.getUserAccount(), productPage.getUserPassword());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(500,"创建账号密码失败");
            }
        }
        boolean b = this.saveOrUpdate(productPage);
        if (b){
            return CommonResult.success(productPage);
        }else {
            return CommonResult.error(500,"操作失败");
        }
    }
}
