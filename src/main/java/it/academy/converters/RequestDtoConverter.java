package it.academy.converters;

import it.academy.dto.ChangeRequestDto;
import it.academy.dto.CreateRequestDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.util.ParameterFinder;

import javax.servlet.http.HttpServletRequest;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Messages.BLANK_STRING;
import static it.academy.util.constants.ParameterNames.*;

public class RequestDtoConverter {

    private RequestDtoConverter() {
    }

    public static CreateRequestDto getCreateRequestDtoPayMoney(HttpServletRequest req) {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int sumAdvance = ParameterFinder.getNumberValueFromParameter(req, SUM_ADVANCE_PARAM, ZERO_INT_VALUE);
        int sumForWork = ParameterFinder.getNumberValueFromParameter(req, SUM_FOR_WORK_PARAM, ZERO_INT_VALUE);
        int calculationDebt=ParameterFinder.getNumberValueFromParameter(req, CALCULATION_DEBT_PARAM, ZERO_INT_VALUE);

        return CreateRequestDto.builder()
                   .id(calculationId)
                   .int1(sumAdvance)
                   .int2(sumForWork)
                   .int3(calculationDebt)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateProject(HttpServletRequest req) {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        String city = ParameterFinder.getStringValueFromParameter(req, CITY_PARAM, BLANK_STRING);
        String street = ParameterFinder.getStringValueFromParameter(req, STREET_PARAM, BLANK_STRING);
        String building = ParameterFinder.getStringValueFromParameter(req, BUILDING_PARAM, BLANK_STRING);

        return CreateRequestDto.builder()
                   .id(developerId)
                   .name(projectName)
                   .city(city)
                   .street(street)
                   .building(building)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateUser(HttpServletRequest req) {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        String name = req.getParameter(NAME_PARAM);
        String city = req.getParameter(CITY_PARAM);
        String street = req.getParameter(STREET_PARAM);
        String building = req.getParameter(BUILDING_PARAM);


        return CreateRequestDto.builder()
                   .email(email)
                   .password(password)
                   .name(name)
                   .city(city)
                   .street(street)
                   .building(building)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateChapter(HttpServletRequest req) {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING).toUpperCase();
        int chapterPrice = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PRICE_PARAM, ZERO_INT_VALUE);

        return CreateRequestDto.builder()
                   .id(projectId)
                   .name(chapterName)
                   .int1(chapterPrice)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoChangeProjectStatus(HttpServletRequest req) {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus newStatus = ParameterFinder.getProjectStatusFromParameter(req, NEW_PROJECT_STATUS_PARAM, null);

        return ChangeRequestDto.builder()
                   .id(projectId)
                   .status(newStatus)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoChangeProposalStatus(HttpServletRequest req) {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, status);
        String showByChapter = req.getParameter(SHOW_PROPOSAL_LIST_BY_CHAPTER_PARAM);

        return ChangeRequestDto.builder()
                   .id(proposalId)
                   .status(newStatus)
                   .name(showByChapter)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateProposal(HttpServletRequest req) {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);

        return CreateRequestDto.builder()
                   .id(chapterId)
                   .secondId(contractorId)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateCalculation(HttpServletRequest req) {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int year = ParameterFinder.getNumberValueFromParameter(req, YYYY_PARAM, ZERO_INT_VALUE);
        int month = ParameterFinder.getNumberValueFromParameter(req, MM_PARAM, ZERO_INT_VALUE);
        int workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_PLAN_PARAM, ZERO_INT_VALUE);

        return CreateRequestDto.builder()
                   .id(chapterId)
                   .int1(year)
                   .int2(month)
                   .int3(workPrice)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoUpdateWorkPrice(HttpServletRequest req) {

        long calculationId = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_ID_PARAM, ZERO_LONG_VALUE);
        int workPrice = ParameterFinder.getNumberValueFromParameter(req, WORK_PRICE_FACT_PARAM, ZERO_INT_VALUE);

        return ChangeRequestDto.builder()
                   .id(calculationId)
                   .count(workPrice)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoSetProposalStatus(HttpServletRequest req) {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus newStatus = ParameterFinder.getProposalStatusFromParameter(req, NEW_PROPOSAL_STATUS_PARAM, null);

        return ChangeRequestDto.builder()
                   .id(proposalId)
                   .status(newStatus)
                   .build();
    }

    public static CreateRequestDto getCreateRequestDtoCreateAdmin(HttpServletRequest req) {

        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        return CreateRequestDto.builder()
                   .email(email)
                   .password(password)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoChangeUserStatus(HttpServletRequest req) {

        long userId = ParameterFinder.getNumberValueFromParameter(req, USER_ID_PARAM, ZERO_LONG_VALUE);
        UserStatus newStatus = ParameterFinder.getUserStatusFromParameter(req, NEW_USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        String updatingUserRole = req.getParameter(ROLE_OF_UPDATING_USER_PARAM);
        return ChangeRequestDto.builder()
                   .id(userId)
                   .status(newStatus)
                   .name(updatingUserRole)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoDeleteUser(HttpServletRequest req) {

        long userId = ParameterFinder.getNumberValueFromParameter(req, USER_ID_PARAM, ZERO_LONG_VALUE);
        String role = req.getParameter(ROLE_OF_UPDATING_USER_PARAM);

        return ChangeRequestDto.builder()
                   .id(userId)
                   .name(role)
                   .build();
    }

    public static ChangeRequestDto getChangeRequestDtoDeleteProposal(HttpServletRequest req) {

        long proposalId = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_ID_PARAM, ZERO_LONG_VALUE);
        String toDo = req.getParameter(SHOW_PROPOSAL_LIST_BY_CONTRACTOR_PARAM);

        return ChangeRequestDto.builder()
                   .id(proposalId)
                   .name(toDo)
                   .build();
    }
}
