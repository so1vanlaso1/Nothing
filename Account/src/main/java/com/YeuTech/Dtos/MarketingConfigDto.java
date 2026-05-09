package com.YeuTech.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarketingConfigDto(
                @JsonProperty("domain_name") String domainName,

                @JsonProperty("industry_type") String industryType,

                @JsonProperty("relevant_content") String relevantContent,

                @JsonProperty("posting_schedule") String postingSchedule) {
}
