package it.academy.servlet.contractorServlets;

import it.academy.controller.ContractorController;
import it.academy.controller.impl.ContractorControllerImpl;
import it.academy.dto.ProjectDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.util.Constants;
import it.academy.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "mainContractorServlet", urlPatterns = "/main_contractor_servlet")
public class MainContractorServlet extends HttpServlet {

    ContractorController controller = new ContractorControllerImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Long id = (Long) session.getAttribute("id");
        String todoStr = req.getParameter("todo");
        if (WhatToDo.SHOW_PROJECTS.toString().equals(todoStr)) {

            ProjectStatus status = Util.getProjectStatus(req, "project_status", Constants.DEFAULT_PROJECT_STATUS);
            session.setAttribute("project_status", status);

            Integer page = Util.getNumberValueFromParametr(req, "project_page", Constants.FIRST_PAGE_NUMBER);
            page=page==Constants.ZERO_PAGE_NUMBER?Constants.FIRST_PAGE_NUMBER:page;


            int count = Util.getNumberValueFromParametr(req, "project_count_on_page", Constants.DEFAULT_COUNT_ON_PAGE_5);
            session.setAttribute("project_count_on_page", count);


            List<ProjectDto> projectDtoList = controller.getMyProjects(id, status, page, count);
            session.setAttribute("project_page", page);
            req.setAttribute("project_dto_list", projectDtoList);


            getServletContext().getRequestDispatcher("/contractor_pages/list_with_projects.jsp").forward(req, resp);

        } else if (WhatToDo.SHOW_DEVELOPERS.toString().equals(todoStr)) {

        } else if (WhatToDo.SHOW_PROPOSALS.toString().equals(todoStr)) {

        } else if (WhatToDo.TRY_TO_CHOOSE_NEW_PROJECT.toString().equals(todoStr)) {

        } else {

        }
    }

}
