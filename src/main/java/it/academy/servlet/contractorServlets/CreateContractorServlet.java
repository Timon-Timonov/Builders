package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ContractorDto;
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
@WebServlet(name = "createContractorServlet", urlPatterns = "/create_contractor_servlet")
public class CreateContractorServlet extends HttpServlet {

    ContractorController contractorController = new ContractorControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        String building = req.getParameter("building");

        ContractorDto contractorDto = new ContractorDto();
        try {
            contractorDto = contractorController.createContractor(email, password, name, city, street, building);
        } catch (NotCreateDataInDbException e) {
            req.setAttribute("message", "Account not create!");
            getServletContext().getRequestDispatcher("/exception_pages/exception_creation_page_2.jsp").forward(req, resp);
        } catch (EmailOccupaidException e) {
            log.debug("Email " + email + " is occupaid!", e);
            req.setAttribute("message", "Email " + email + " is occupaid!");
            getServletContext().getRequestDispatcher("/exception_pages/exception_creation_page_2.jsp").forward(req, resp);
        }

        if (contractorDto.getId() != null) {
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("role", Roles.CONTRACTOR);
            getServletContext().getRequestDispatcher("/contractor_pages/main.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "Account not create!");
            getServletContext().getRequestDispatcher("/exception_pages/exception_creation_page_2.jsp").forward(req, resp);
        }
    }
}
