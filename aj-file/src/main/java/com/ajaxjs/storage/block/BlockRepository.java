package com.ajaxjs.storage.block;


import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.storage.block.model.ContentBlock;
import com.google.common.collect.ImmutableMap;


/**
 * @author Helfen
 */
public interface BlockRepository extends BaseMapper<ContentBlock> {
    default ContentBlock selectByMd5(String md5) {
        return SqlHelper.sqlSession(ContentBlock.class).selectOne(
                "selectByMd5WithOutApp",
                ImmutableMap.builder().put("md5", md5).build());
    }

    default ContentBlock selectByMd5(String appId, String storage, String md5) {
        return SqlHelper.sqlSession(ContentBlock.class).selectOne(
                "selectByMd5WithApp",
                ImmutableMap.builder().put("appId", appId).put("storage", storage).put("md5", md5).build());
    }

    @Select("select ref_count from ufs_storage_block where id = #{blockId}")
    int refCount(long blockId);

    @Update("update ufs_storage_block set ref_count = ref_count + 1 where id = #{blockId}")
    boolean retain(long blockId);

    @Update("update ufs_storage_block set ref_count = ref_count -1 where id = #{blockId}")
    boolean release(long blockId);

    @Delete("delete from ufs_storage_block where id = #{blockId} and ref_count <= 0")
    boolean deleteByIdAndOrphan(long blockId);
}
