package cn.snowsoft.iot.module.job.dao;

import cn.snowsoft.iot.module.job.core.model.XxlJobGroup;
import cn.snowsoft.iot.module.job.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupDao {

    public List<XxlJobGroup> findAll();

    public List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(XxlJobGroup xxlJobGroup);

    public int update(XxlJobGroup xxlJobGroup);

    public int remove(@Param("id") int id);

    public XxlJobGroup load(@Param("id") int id);

    public XxlJobGroup loadByAppName(@Param("appName") String appName);

    public List<XxlJobGroup> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("appname") String appname,
                                      @Param("title") String title);

    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("title") String title);

    @Select("select * from xxl_job_info where relevance_id = #{relevanceId} and id = #{jobId}")
    List<XxlJobInfo> getByRelevanceId(@Param("relevanceId") Long relevanceId, @Param("jobId") int jobId);
}
