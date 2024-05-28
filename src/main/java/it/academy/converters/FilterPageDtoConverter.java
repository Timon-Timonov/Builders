package it.academy.converters;

import it.academy.dto.FilterPageDto;
import it.academy.pojo.enums.ProjectStatus;
import it.academy.pojo.enums.ProposalStatus;
import it.academy.pojo.enums.UserStatus;
import it.academy.util.ParameterFinder;
import it.academy.util.constants.Constants;

import javax.servlet.http.HttpServletRequest;

import static it.academy.util.constants.Constants.*;
import static it.academy.util.constants.ParameterNames.*;

public final class FilterPageDtoConverter {

    private FilterPageDtoConverter() {
    }

    public static FilterPageDto getPageRequestDtoShowProposals(HttpServletRequest req) {

        return getFilterPageDtoProposals(req, ID_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetProposalsByContractor(HttpServletRequest req) {

        return getFilterPageDtoProposals(req, CONTRACTOR_ID_PARAM);
    }

    public static FilterPageDto getPageRequestDtoShowContractors(HttpServletRequest req) {

        return getFilterPageDtoLegelEntity(req, CONTRACTOR_PAGE_PARAM, CONTRACTOR_COUNT_ON_PAGE_PARAM);
    }

    public static FilterPageDto getPageRequestDtoShowDevelopers(HttpServletRequest req) {

        return getFilterPageDtoLegelEntity(req, DEVELOPER_PAGE_PARAM, DEVELOPER_COUNT_ON_PAGE_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetChaptersByContractorAndDeveloper(HttpServletRequest req) {

        return getFilterPageDtoCommonEntities(req, CONTRACTOR_ID_PARAM, CHAPTER_PAGE_PARAM, CHAPTER_COUNT_ON_PAGE_PARAM, CHAPTER_CONTRACTOR_NAME_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetProjectsByDeveloperAndContractor(HttpServletRequest req) {

        return getFilterPageDtoCommonEntities(req, DEVELOPER_ID_PARAM, PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM, DEVELOPER_NAME_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetProjectsByDeveloper(HttpServletRequest req) {

        return getFilterPageDtoEntitiesByUser(req, DEVELOPER_ID_PARAM, PROJECT_PAGE_PARAM, PROJECT_COUNT_ON_PAGE_PARAM, DEVELOPER_NAME_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetFreeChapters(HttpServletRequest req) {

        return getFilterPageDtoEntitiesByUser(req, ID_PARAM, CHAPTER_PAGE_PARAM, CHAPTER_COUNT_ON_PAGE_PARAM, CHAPTER_NAME_PARAM);
    }

    public static FilterPageDto getFilterPageDtoGetProposalsByChapter(HttpServletRequest req) {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        ProposalStatus status = ParameterFinder.getProposalStatusFromParameter(req, PROPOSAL_STATUS_PARAM, DEFAULT_PROPOSAL_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, PROPOSAL_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);

        return FilterPageDto.builder()
                   .id(chapterId)
                   .status(status)
                   .page(page)
                   .count(count)
                   .name(chapterName)
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

    public static FilterPageDto getPageRequestDtoShowLegalEntities(HttpServletRequest req, String userPageParam, String userCountOnPageParam) {

        UserStatus status = ParameterFinder.getUserStatusFromParameter(req, USER_STATUS_PARAM, DEFAULT_USER_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, userPageParam, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, userCountOnPageParam, DEFAULT_COUNT_ON_PAGE_5);
        String search = ParameterFinder.getSearchStringValue(req);

        return FilterPageDto.builder()
                   .status(status)
                   .page(page)
                   .count(count)
                   .search(search)
                   .build();
    }

    public static FilterPageDto getFilterPageDtoGetCalculationsByChapter(HttpServletRequest req) {

        long chapterId = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_ID_PARAM, ZERO_LONG_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CALCULATION_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, CHAPTER_NAME_PARAM, BLANK_STRING);

        return FilterPageDto.builder()
                   .id(chapterId)
                   .page(page)
                   .count(count)
                   .name(chapterName)
                   .build();
    }

    public static FilterPageDto getFilterPageDtoGetChaptersByProject(HttpServletRequest req) {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);

        return FilterPageDto.builder()
                   .id(projectId)
                   .name(projectName)
                   .build();
    }

    public static FilterPageDto getFilterPageDtoGetChaptersByProjectAndContractor(HttpServletRequest req) {

        long projectId = ParameterFinder.getNumberValueFromParameter(req, PROJECT_ID_PARAM, ZERO_LONG_VALUE);
        String projectName = ParameterFinder.getStringValueFromParameter(req, PROJECT_NAME_PARAM, BLANK_STRING);
        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);

        return FilterPageDto.builder()
                   .id(projectId)
                   .secondId(contractorId)
                   .name(projectName)
                   .build();
    }

    public static FilterPageDto getFilterPageDtoGetChaptersByContractor(HttpServletRequest req) {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, CONTRACTOR_ID_PARAM, ZERO_LONG_VALUE);
        int page = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_PAGE_PARAM, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, CHAPTER_COUNT_ON_PAGE_PARAM, DEFAULT_COUNT_ON_PAGE_5);

        return FilterPageDto.builder()
                   .id(contractorId)
                   .page(page)
                   .count(count)
                   .build();
    }

    private static FilterPageDto getFilterPageDtoProposals(HttpServletRequest req, String idParam) {

        long id = ParameterFinder.getNumberValueFromParameter(req, idParam, ZERO_LONG_VALUE);
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

    private static FilterPageDto getFilterPageDtoLegelEntity(HttpServletRequest req, String developerPageParam, String developerCountOnPageParam) {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, developerPageParam, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, developerCountOnPageParam, DEFAULT_COUNT_ON_PAGE_5);
        String search = ParameterFinder.getSearchStringValue(req);

        return FilterPageDto.builder()
                   .id(contractorId)
                   .status(status)
                   .page(page)
                   .count(count)
                   .search(search)
                   .build();
    }

    private static FilterPageDto getFilterPageDtoCommonEntities(HttpServletRequest req, String developerIdParam, String projectPageParam, String projectCountOnPageParam, String developerNameParam) {

        long developerId = ParameterFinder.getNumberValueFromParameter(req, developerIdParam, ZERO_LONG_VALUE);
        long contractorId = ParameterFinder.getNumberValueFromParameter(req, ID_PARAM, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, projectPageParam, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, projectCountOnPageParam, DEFAULT_COUNT_ON_PAGE_5);
        String developerName = ParameterFinder.getStringValueFromParameter(req, developerNameParam, BLANK_STRING);

        return FilterPageDto.builder()
                   .id(developerId)
                   .secondId(contractorId)
                   .name(developerName)
                   .status(status)
                   .page(page)
                   .count(count)
                   .build();
    }

    private static FilterPageDto getFilterPageDtoEntitiesByUser(HttpServletRequest req, String idParam, String chapterPageParam, String chapterCountOnPageParam, String chapterNameParam) {

        long contractorId = ParameterFinder.getNumberValueFromParameter(req, idParam, ZERO_LONG_VALUE);
        ProjectStatus status = ParameterFinder.getProjectStatusFromParameter(req, PROJECT_STATUS_PARAM, DEFAULT_PROJECT_STATUS);
        int page = ParameterFinder.getNumberValueFromParameter(req, chapterPageParam, FIRST_PAGE_NUMBER);
        int count = ParameterFinder.getNumberValueFromParameter(req, chapterCountOnPageParam, DEFAULT_COUNT_ON_PAGE_5);
        String chapterName = ParameterFinder.getStringValueFromParameter(req, chapterNameParam, BLANK_STRING);

        return FilterPageDto.builder()
                   .id(contractorId)
                   .status(status)
                   .page(page)
                   .count(count)
                   .name(chapterName)
                   .build();
    }
}
