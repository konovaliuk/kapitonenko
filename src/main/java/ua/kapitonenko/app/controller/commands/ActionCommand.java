package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public interface ActionCommand {
	ResponseParams execute(RequestWrapper request)
			throws ServletException, IOException;
}
