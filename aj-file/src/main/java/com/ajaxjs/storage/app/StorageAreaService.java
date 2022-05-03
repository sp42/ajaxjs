package com.ajaxjs.storage.app;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.storage.app.model.Organization;
import com.ajaxjs.storage.app.model.StorageArea;

public class StorageAreaService {
	public interface StorageAreaRespository {
//	    @Select("select count(1) from ufs_storage_area where id = #{id} limit 1")
		boolean exists(Serializable id);

//	    @Select("select * from ufs_storage_area where org_ids like CONCAT('%', #{orgId},'%')")
		StorageArea getByOrgId(Serializable orgId);
	}

//	@Autowired(required = false)
//	private SecurityContext securityContext;

	public StorageArea create(StorageArea storageArea) {
		storageAreaRespository.insert(storageArea);
		return storageArea;
	}

	public StorageArea getByOrgId(String orgId) {
		StorageArea storageArea = storageAreaRespository.getByOrgId(orgId);

		if (Objects.isNull(storageArea)) {
			Map<String, Organization> organizationMap = securityContext.getOrganizationMap(orgId);
			Organization organization = organizationMap.get(orgId);

			while (java.util.Objects.isNull(storageArea) && java.util.Objects.nonNull(organizationMap.get(organization.getParentId()))) {
				Organization parentOrganization = organizationMap.get(organization.getParentId());
				storageArea = storageAreaRespository.getByOrgId(parentOrganization.getId());
				organization = parentOrganization;
			}
		}

		return storageArea;
	}
}
