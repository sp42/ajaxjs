-- Dumping structure for function ajaxjs.getAllChildren
DELIMITER //
CREATE FUNCTION `getAllChildren`(
	`rootId` INT,
	`tableName` VARCHAR(50)
) RETURNS text CHARSET utf8mb4 COLLATE utf8mb4_bin
BEGIN
	DECLARE P_TEMP VARCHAR(1000);
	DECLARE C_TEMP VARCHAR(1000);
	
	SET P_TEMP = '0';
	SET C_TEMP = CAST(rootId AS CHAR);
	
	WHILE C_TEMP IS NOT NULL DO
		SET P_TEMP = CONCAT(P_TEMP, ',', C_TEMP);
		
		IF tableName = 'sys_datadict' THEN
			SELECT GROUP_CONCAT(id) INTO C_TEMP FROM ajaxjs.sys_datadict WHERE FIND_IN_SET(parentId, C_TEMP) > 0; 
		ELSEIF tableName = 'sys_datadict1' THEN
			SELECT GROUP_CONCAT(id) INTO C_TEMP FROM ajaxjs.sys_datadict WHERE FIND_IN_SET(parentId, C_TEMP) > 0; 
		ELSE 
			SET C_TEMP = NULL;
		END IF; -- if结束
	END WHILE; 
	 
	RETURN P_TEMP; 
END//
DELIMITER ;
