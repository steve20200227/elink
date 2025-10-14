package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductPointPageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductPointDO;
import cn.snowsoft.iot.module.cps.dal.mysql.product.ProductPointMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductPointServiceImpl extends ServiceImpl<ProductPointMapper, ProductPointDO> implements ProductPointService {

    @Override
    public IPage<ProductPointDO> selectPointPage(ProductPointPageVO point) {
        // 编写分页逻辑
        IPage<ProductPointDO> page = new Page<>(point.getPageNo(), point.getPageSize());
        return baseMapper.selectPointPage(page, point);
    }

    @Override
    public CommonResult submit(ProductPointDO point) {
        List<ProductPointDO> pointList = checkCode(point);
        if (!pointList.isEmpty()){
            // 抛出异常
            return CommonResult.error(207, "点位编码已存在");
        }
        saveOrUpdate(point);
        return CommonResult.success(point);
    }

    @Override
    public Boolean deleteLogic(List<Long> ids) {
        // 实现逻辑删除
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public void removePoint(Long id) {
        lambdaUpdate().eq(ProductPointDO::getProductId, id).set(ProductPointDO::getDeleted, 1).update();
    }

    @Override
    public List<ProductPointDO> getByProductId(Long productId) {
        return baseMapper.selectList(new LambdaQueryWrapper<ProductPointDO>()
                .eq(ProductPointDO::getProductId, productId)
                .eq(ProductPointDO::getPointType, "pointFeature")
                .eq(ProductPointDO::getIsEnable, 1));
    }

    private List<ProductPointDO> checkCode(ProductPointDO point) {
        LambdaQueryWrapper<ProductPointDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (point.getPointCode().isEmpty()){
            // 抛出异常
            throw new RuntimeException("点位编码不能为空");
        }
        lambdaQueryWrapper.eq(ProductPointDO::getPointCode, point.getPointCode());
        // 只有在该产品下的点位编码不可重复
        lambdaQueryWrapper.eq(ProductPointDO::getProductId, point.getProductId());
        //id不为空 是修改 排除当前数据
        if (point.getId() != null){
            lambdaQueryWrapper.ne(ProductPointDO::getId, point.getId());
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
