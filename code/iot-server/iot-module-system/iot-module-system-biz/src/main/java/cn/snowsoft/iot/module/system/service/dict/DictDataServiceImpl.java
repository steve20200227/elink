package cn.snowsoft.iot.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import cn.snowsoft.iot.framework.common.enums.CommonStatusEnum;
import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.framework.common.util.collection.CollectionUtils;
import cn.snowsoft.iot.framework.common.util.object.BeanUtils;
import cn.snowsoft.iot.module.system.api.dict.dto.DictDataRespDTO;
import cn.snowsoft.iot.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import cn.snowsoft.iot.module.system.controller.admin.dict.vo.data.DictDataSaveReqVO;
import cn.snowsoft.iot.module.system.dal.dataobject.dict.DictDataDO;
import cn.snowsoft.iot.module.system.dal.dataobject.dict.DictTypeDO;
import cn.snowsoft.iot.module.system.dal.mysql.dict.DictDataMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.snowsoft.iot.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.snowsoft.iot.module.system.enums.ErrorCodeConstants.*;

/**
 * 字典数据 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class DictDataServiceImpl implements DictDataService {
    @Autowired
    private DictDataServiceImpl dictDataServiceImpl;

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictDataDO::getDictType)
            .thenComparingInt(DictDataDO::getSort);

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public List<DictDataDO> getDictDataList(Integer status, String dictType) {
        List<DictDataDO> list = dictDataMapper.selectListByStatusAndDictType(status, dictType);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    /**
     * 监听容器上下文刷新事件，目的是将平台参数字典加载到缓存，供告警消息发送时使用
     * @param event
     */
    @EventListener(ContextRefreshedEvent.class)
    public void handleContextRefresh(ContextRefreshedEvent event) {
        dictDataServiceImpl.cacheMessageParamsDict("message_params");
    }

    @CachePut(value = "dictCache", key = "#dictType")
    public List<DictDataRespDTO> cacheMessageParamsDict(String dictType) {
        List<DictDataRespDTO> list = dictDataMapper.selectListByStatusAndDictTypeCopy(CommonStatusEnum.ENABLE.getStatus(), dictType);
        //list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO pageReqVO) {
        return dictDataMapper.selectPage(pageReqVO);
    }

    @Override
    public DictDataDO getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(DictDataSaveReqVO createReqVO) {
        // 校验字典类型有效
        validateDictTypeExists(createReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(null, createReqVO.getDictType(), createReqVO.getValue());

        // 插入字典类型
        DictDataDO dictData = BeanUtils.toBean(createReqVO, DictDataDO.class);
        dictDataMapper.insert(dictData);
        //如果是message_params字典的新增, 那么更新缓存数据
        if ("message_params".equals(createReqVO.getDictType())) {
            dictDataServiceImpl.cacheMessageParamsDict("message_params");
        }
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictDataExists(updateReqVO.getId());
        // 校验字典类型有效
        validateDictTypeExists(updateReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(updateReqVO.getId(), updateReqVO.getDictType(), updateReqVO.getValue());

        // 更新字典类型
        DictDataDO updateObj = BeanUtils.toBean(updateReqVO, DictDataDO.class);
        dictDataMapper.updateById(updateObj);
        //如果是message_params字典的更新, 那么更新缓存数据
        if ("message_params".equals(updateReqVO.getDictType())) {
            dictDataServiceImpl.cacheMessageParamsDict("message_params");
        }
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        String dictType = validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
        //如果是message_params字典的更新, 那么更新缓存数据
        if ("message_params".equals(dictType)) {
            dictDataServiceImpl.cacheMessageParamsDict("message_params");
        }
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

    @VisibleForTesting
    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public String validateDictDataExists(Long id) {
        if (id == null) {
            return null;
        }
        DictDataDO dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
        return dictData.getDictType();
    }

    @VisibleForTesting
    public void validateDictTypeExists(String type) {
        DictTypeDO dictType = dictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, DictDataDO> dictDataMap = CollectionUtils.convertMap(
                dictDataMapper.selectByDictTypeAndValues(dictType, values), DictDataDO::getValue);
        // 校验
        values.forEach(value -> {
            DictDataDO dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public DictDataDO getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public DictDataDO parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<DictDataDO> getDictDataListByDictType(String dictType) {
        List<DictDataDO> list = dictDataMapper.selectList(DictDataDO::getDictType, dictType);
        list.sort(Comparator.comparing(DictDataDO::getSort));
        return list;
    }

    @Override
    public CommonResult getDictDataLists(List<String> dictTypes) {
        List<DictDataDO> dictDataDOS = dictDataMapper.getDictDataLists(dictTypes);
        Map<String, List<DictDataDO>> map = new HashMap<>();
        dictDataDOS.forEach(item -> {
            String dictType = item.getDictType();
            if (map.containsKey(dictType)) {
                List<DictDataDO> dataDOS = map.get(dictType);
                dataDOS.add(item);
                map.put(dictType, dataDOS);
            } else {
                List<DictDataDO> dataDOList = new ArrayList<>();
                dataDOList.add(item);
                map.put(dictType, dataDOList);
            }
        });
        return CommonResult.success(map);
    }
}
