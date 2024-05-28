package it.academy.filter;

import it.academy.dto.UserDto;
import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.service.AdminService;
import it.academy.service.impl.AdminServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ServletURLs.*;

@WebFilter(urlPatterns = {"/*"})
public class LogInFilter implements Filter {

    private final AdminService service = AdminServiceImpl.getInstance();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String servletPath = req.getServletPath();

        if (servletPath.contains(DOT_JSP) || servletPath.contains(NOT_FILTERED)) {
            filterChain.doFilter(req, resp);
        } else {
            boolean itNeedInRedirectToLogOut = true;
            try {
                HttpSession session = req.getSession();
                Roles role = (Roles) session.getAttribute(ROLE_PARAM);
                Long userId = (Long) session.getAttribute(ID_PARAM);
                String email = (String) session.getAttribute(EMAIL_PARAM);
                String password = (String) session.getAttribute(PASSWORD_PARAM);
                itNeedInRedirectToLogOut = true;

                if (userId != null && password != null && role != null && email != null) {
                    UserDto userDto = null;
                    if (!email.isBlank()) {
                        userDto = service.getUser(email);
                    }
                    if (userDto != null) {
                        if (userId.equals(userDto.getId()) &&
                                password.equals(userDto.getPassword()) &&
                                role.equals(userDto.getUserRole()) &&
                                UserStatus.ACTIVE.equals(userDto.getUserStatus())) {
                            switch (role) {
                                case ADMIN:
                                    if (servletPath.contains(ADMINISTRATOR_SERVLET)) {
                                        filterChain.doFilter(req, resp);
                                        itNeedInRedirectToLogOut = false;
                                    }
                                    break;
                                case DEVELOPER:
                                    if (servletPath.contains(DEVELOPER_SERVLET)) {
                                        filterChain.doFilter(req, resp);
                                        itNeedInRedirectToLogOut = false;
                                    }
                                    break;
                                case CONTRACTOR:
                                    if (servletPath.contains(CONTRACTOR_SERVLET)) {
                                        filterChain.doFilter(req, resp);
                                        itNeedInRedirectToLogOut = false;
                                    }
                                    break;
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            if (itNeedInRedirectToLogOut) {
                req.getRequestDispatcher(SLASH_STRING + LOGOUT_SERVLET).forward(req, resp);
            }
        }
    }
}
