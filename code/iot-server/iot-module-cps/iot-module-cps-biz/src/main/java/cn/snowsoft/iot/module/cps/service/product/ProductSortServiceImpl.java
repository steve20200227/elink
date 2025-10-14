package cn.snowsoft.iot.module.cps.service.product;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.product.vo.ProductSortVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductDO;
import cn.snowsoft.iot.module.cps.dal.dataobject.product.ProductSortDO;
import cn.snowsoft.iot.module.cps.dal.mysql.product.ProductSortMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSortServiceImpl extends ServiceImpl<ProductSortMapper, ProductSortDO> implements ProductSortService {

    private final ProductServiceImpl productService;

    public ProductSortServiceImpl(ProductServiceImpl productService) {
        this.productService = productService;
    }


    @Override
    public ProductSortVO saveData(ProductSortVO productSortVO) {
        if (productSortVO.getId() != null) {
            ProductSortDO productSortDO = getById(productSortVO.getId());
            String sortName = productSortVO.getSortName();
            BeanUtils.copyProperties(productSortDO, productSortVO);
            productSortVO.setSortName(sortName);
        }
        // 判断是否是根节点
        if (productSortVO.getParentId() != null) {
            ProductSortDO parent = getById(productSortVO.getParentId());
            if (ObjectUtils.isNotEmpty(parent)) {
                List<String> progenitorIds = new ArrayList<>(Arrays.asList(parent.getProgenitorId().split(",")));
                progenitorIds.add(String.valueOf(parent.getId()));
                // 构造为','分割的字符串
                productSortVO.setProgenitorId(String.join(",", progenitorIds));
            }
        } else {
            productSortVO.setParentId(0L);
            productSortVO.setProgenitorId("0");
        }
        saveOrUpdate(productSortVO);
        return productSortVO;
    }

    @Override
    public CommonResult<Boolean> verifyRemove(Long id) {
        List<ProductSortDO> productSortDOS = baseMapper.selectList(new LambdaQueryWrapper<ProductSortDO>().like(ProductSortDO::getProgenitorId, id));
        List<ProductDO> productDOList = productService.getByProductSortId(id);
        if (productDOList.isEmpty() && productSortDOS.isEmpty()) {
            return CommonResult.success(removeById(id));
        }
        return CommonResult.success(false);
    }

    @Override
    public List<ProductSortVO> getAllProductSorts() {
        List<ProductSortDO> collect = list().stream()
                .sorted(Comparator.comparing(ProductSortDO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return buildTree(collect, 0L);
    }

    public List<ProductSortVO> buildTree(List<ProductSortDO> productSortDOList, Long parentId) {
        Map<Long, List<ProductSortDO>> groupedByParentId = productSortDOList.stream()
                .collect(Collectors.groupingBy(ProductSortDO::getParentId));

        List<ProductSortVO> productSortVOS = new ArrayList<>();
        List<ProductSortDO> children = groupedByParentId.getOrDefault(parentId, Collections.emptyList());

        for (ProductSortDO e : children) {
            ProductSortVO productSortVO = new ProductSortVO();
            BeanUtils.copyProperties(e, productSortVO);
            productSortVO.setChildren(buildTree(productSortDOList, e.getId()));
            productSortVOS.add(productSortVO);
        }

        return productSortVOS;
    }
}
