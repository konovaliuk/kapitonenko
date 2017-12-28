package ua.kapitonenko.controller.commands;

import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public class HomeAction implements ActionCommand {
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		//return Pages.HOME;
		return null;
	}
}
