package it.academy.servlet.contractorServlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GetMyChaptersServlet",urlPatterns = "/get_my_chapters_servlet")
public class GetMyChaptersServlet  extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {









        getServletContext().getRequestDispatcher("/contractor_pages/list_with_chapters.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String chapterId=req.getParameter("chapter_id");






        getServletContext().getRequestDispatcher("/contractor_pages/list_with_calculations.jsp").forward(req, resp);
    }
}
