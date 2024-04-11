package it.academy.servlet;

import it.academy.pojo.enums.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "toMainServlet",urlPatterns = "/tomain_servlet")
public class ToMainServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session=req.getSession();
        Roles role= (Roles) session.getAttribute("role");
        String url;
        switch (role){
            case DEVELOPER:
                url="/developer_pages/main.jsp";
                break;
            case CONTRACTOR:
                url="/contractor_pages/main.jsp";
                break;
            case ADMIN:
                url="/admin_pages/main.jsp";
                break;
            default:
                url="/logout_servlet";
        }

        clearSession(session);
        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

    protected static void clearSession(HttpSession session) {


        session.removeAttribute("todo");
        session.removeAttribute("project_count_on_page");
        session.removeAttribute("project_page");
        session.removeAttribute("project_status");


        session.removeAttribute("chapter_count_on_page");
        session.removeAttribute("chapter_page");
        session.removeAttribute("chapter_status");
    }
}
