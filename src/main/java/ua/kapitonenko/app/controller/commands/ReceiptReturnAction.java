package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public class ReceiptReturnAction implements ActionCommand {
	@Override
	public ResponseParams execute(RequestWrapper requestWrapper) throws ServletException, IOException {
		return null;
	}
}
