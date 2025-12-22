package com.archive.archive_project_backend.dto.req.SearchResDto;


import com.archive.archive_project_backend.constants.SearchConstants;
import com.archive.archive_project_backend.model.WritingSearchCond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchReqDto {
    private Integer mainCategoryId;
    private Integer categoryId;
    private String title;
    private String author;
    private String formType;
    private DateRange dateRange;
    private ViewRange viewRange;
    private GreatRange greatRange;
    private Integer page;
    private String sortBy;

    public WritingSearchCond toCond(){
        Boolean isSeries;
        if(formType == null) isSeries = null;
        else if(formType.equals(SearchConstants.SNIPPER)) isSeries = false;
        else if(formType.equals(SearchConstants.SERIES)) isSeries = true;
        else isSeries = null;

        String title = this.title == null || this.title.length() <= 1 ? null : this.title;

        int sortOrderCode = SearchConstants.SORT_ORDER_CODE_MAP.getOrDefault(sortBy, 1);

        return WritingSearchCond.builder()
                .mainCategoryId(this.mainCategoryId)
                .categoryId(this.categoryId)
                .title(title)
                .author(this.author)
                .isSeries(isSeries)
                .from(dateRange == null ? null : dateRange.getFrom())
                .to(dateRange == null ? null : dateRange.getTo())
                .viewMin(viewRange == null  ? null : viewRange.getMin())
                .viewMax(viewRange == null  ? null :viewRange.getMax())
                .greatMax(greatRange == null  ? null :greatRange.getMax())
                .greatMin(greatRange == null  ? null :greatRange.getMin())
                .limit(10)
                .offset(10 * (this.page - 1))
                .sortCode(sortOrderCode)
                .build();
    }
}






