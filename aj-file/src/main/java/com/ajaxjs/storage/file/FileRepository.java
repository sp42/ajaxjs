package com.ajaxjs.storage.file;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Helfen
 */
public interface FileRepository extends BaseMapper<FileStatus> {
    @Select("select count(1) from ufs_storage_file where id = #{fileId} limit 1")
    boolean exists(long fileId);

    default Map<String, String> getMetdata(long fileId) {
        Map<String, String> map = new HashMap<>();

        SqlSession session = SqlHelper.sqlSession(FileStatus.class);
        List<Map<String, String>> rows = session.selectList("selectMetadata", fileId);
        rows.forEach(it -> {
            map.put(it.get("meta_key"), it.get("meta_value"));
        });

        return map;
    }

    default void setMetadata(long fileId, Map<String, String> fileMetadata) {
        if (fileMetadata == null || fileMetadata.isEmpty())
            return;

        try (SqlSession session = SqlHelper.sqlSessionBatch(FileStatus.class)) {
            Map<String, Object> params = new HashMap<>();
            params.put("fileId", fileId);
            params.put("keys", fileMetadata.keySet().stream().filter(it -> it != null).collect(Collectors.toList()));
            session.delete("deleteMetadata", params);
            for (Map.Entry<String, String> pair : fileMetadata.entrySet()) {
                if (!Strings.isNullOrEmpty(pair.getKey()) && !Strings.isNullOrEmpty(pair.getValue())) {
                    params.put("fileId", fileId);
                    params.put("meta_key", pair.getKey());
                    params.put("meta_value", pair.getValue());
                    session.insert("insertMetadata", params);
                }
            }
            session.commit();
        }
    }

    @Delete("delete from ufs_storage_file_metadata where file_id = #{fileId}")
    void clearMetadata(long fileId);

    boolean updateAccessControlAndContentType(@Param("fileId")long fileId, @Param("accessControl") AccessControl accessControl, @Param("contentType")String contentType);
}
