package com.ajaxjs.storage.app;

import java.util.Date;
import java.util.NoSuchElementException;

import com.ajaxjs.storage.ErrorMessages;
import com.ajaxjs.storage.app.model.Application;
import com.ajaxjs.storage.app.model.DestroyRequest;
import com.ajaxjs.storage.app.model.RegisterRequest;
import com.google.common.eventbus.EventBus;

/**
 * @author Helfen
 */
public class AppService {
	@Autowired
	private AppRepository repository;

	@Autowired(required = false)
	private SecurityContext securityContext;

	@Autowired
	private EventBus eventBus;

	public Application status() {
		Application app = repository.selectById(securityContext.getAppId());
		if (app == null)
			throw new NoSuchElementException(ErrorMessages.APPLICATION_NOT_FOUND);

		return app;
	}

	public Application get(String appId) {
		return repository.selectById(appId);
	}

	public Application register(RegisterRequest request) {
		// String appId = securityContext.getAppId();
		String appId = new Date().getTime() + "";
		if (repository.selectCount(new EntityWrapper<Application>().eq("id", appId)) > 0)
			throw new IllegalArgumentException(ErrorMessages.APPLICATION_EXISTS);

		ApplicationSettings settings = request.getSettings();
		
		if (settings == null)
			settings = new ApplicationSettings();
		else if (settings.getConstraints() == null)
			settings.setConstraints(new ApplicationSettings.Constraints());

		Application app = new Application().setId(appId).setName(request.getName()).setDescription(request.getDescription()).setSettings(settings);
		repository.insert(app);
		eventBus.post(new ApplicationRegisteredEvent(this, app));

		return app;
	}

	public void update(@NotNull() Application app) {
		app.setId(securityContext.getAppId());
		if (!repository.exists(app.getId())) 
			throw new NoSuchElementException();
		
		repository.updateById(app);
	}

	public void destory(DestroyRequest request) {
		if (!repository.exists(securityContext.getAppId()))
			throw new NoSuchElementException();

		repository.deleteById(securityContext.getAppId());
	}
}
