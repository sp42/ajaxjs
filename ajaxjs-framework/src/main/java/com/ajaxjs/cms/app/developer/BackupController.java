package com.ajaxjs.cms.app.developer;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.backup.MysqlExport;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.ZipHelper;

@Path("/admin/backup")
public class BackupController implements IController {
 
	
	@Path("images")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String images(MvcRequest r) {
		String folder = r.mappath("/images");
		ZipHelper.zip(folder, r.mappath("/temp/images.zip"));

		return BaseController.jsonOk("备份图片成功！");
	}

	@Path("site")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String site(MvcRequest r, @NotNull @QueryParam("i") int i) {
		String folder = r.mappath("/");
		String save = r.mappath("/temp/site.zip"), _save = save.replaceAll("/", "\\\\");
		String images = r.mappath("/images").replaceAll("/", "\\\\");
		String lib = r.mappath("/WEB-INF/lib").replaceAll("/", "\\\\");
		String classes = r.mappath("/WEB-INF/classes").replaceAll("/", "\\\\");
		boolean isWithImage = (i & 1) == 1, isWithClasses = (i & 2) == 2, isWithLib = (i & 4) == 4;

		ZipHelper.zip(folder, save, file -> {
			String path = file.toString();

			if (path.equals(images))
				return isWithImage;

			if (path.equals(lib))
				return isWithLib;

			if (path.equals(classes))
				return isWithClasses;

			if (path.equals(_save)) // 排除自己，不然 zip 文件会越来越大
				return false;

			return true;
		});

		return BaseController.jsonOk("备份项目成功！");
	}

	@Path("db")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String db(MvcRequest r) {
		String save = r.mappath("/temp/");
		new MysqlExport(JdbcConnection.getConnection(), "workflow", save).export();

		return BaseController.jsonOk("备份SQL成功！");
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String clear(MvcRequest r) {
		FileHelper.deleteOldWay(r.mappath("/temp/"));
		return BaseController.jsonOk("清理成功！");
	}
}