package ua.kapitonenko.controller.commands;

import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public interface ActionCommand {
	ResponseParams execute(RequestWrapper requestWrapper)
			throws ServletException, IOException;
}
