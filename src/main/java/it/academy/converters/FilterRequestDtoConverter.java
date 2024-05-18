package it.academy.converters;

import it.academy.dto.FilterPageDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.util.ParameterFinder;
import it.academy.util.constants.Constants;

import javax.servlet.http.HttpServletRequest;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.Constants.DEFAULT_COUNT_ON_PAGE_5;
import static it.academy.util.constants.ParameterNames.*;
import static it.academy.util.constants.ParameterNames.CONTRACTOR_COUNT_ON_PAGE_PARAM;

public class FilterRequestDtoConverter {

    private FilterRequestDtoConverter() {
    }

    public static FilterPageDto getPageRequestDtoShowContractors(HttpServletRequest req) {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .id(developerId)
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }

    public static FilterPageDto getPageRequestDtoShowProposals(HttpServletRequest req) {

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .id(id)
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }

    public static FilterPageDto getPageRequestDtoShowProjects(HttpServletRequest req) {

        long id = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROJECT_PAGE_PARAM, Constants.FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROJECT_COUNT_ON_PAGE_PARAM, Constants.DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .id(id)
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }

    public static FilterPageDto getPageRequestDtoShowDevelopers(HttpServletRequest req) {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, DEVELOPER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .id(contractorId)
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }

    public static FilterPageDto getPageRequestDtoShowLegalEntities(HttpServletRequest req, String userPageParam, String userCountOnPageParam) {
        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, userPageParam, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, userCountOnPageParam, DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }
}
