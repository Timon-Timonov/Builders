package it.academy.servlet.developerServlets;

import it.academy.controller.DeveloperController;
import it.academy.controller.impl.DeveloperControllerImpl;
import it.academy.dto.DeveloperDto;
import it.academy.exceptions.EmailOccupaidException;
import it.academy.exceptions.NotCreateDataInDbException;
import it.academy.pojo.enums.Roles;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
@WebServlet(name = "createDeveloperServlet", urlPatterns = "/create_developer_servlet")
public class CreateDeveloperServlet extends HttpServlet {

    DeveloperController developerController = new DeveloperControllerImpl();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        String building = req.getParameter("building");




        DeveloperDto developerDto = new DeveloperDto();
        try {
            developerDto = developerController.createDeveloper(email, password, name, city, street, building);
        } catch (NotCreateDataInDbException e) {
            forwardToException2(req, resp, "Account not create!");
        } catch (EmailOccupaidException e) {
            log.debug("Email " + email + " is occupaid!", e);
            forwardToException2(req, resp, "Email " + email + " is occupaid!");
        }

        if (developerDto.getId() != null) {
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("role", Roles.DEVELOPER);
            getServletContext().getRequestDispatcher("/developer_pages/main.jsp").forward(req, resp);
        } else {
            req.setAttribute("role", Roles.DEVELOPER);
            forwardToException2(req, resp, "Account not create!");
        }
    }

    private void forwardToException2(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {

        req.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/exception_pages/exception_creation_page_2.jsp").forward(req, resp);
    }
}








