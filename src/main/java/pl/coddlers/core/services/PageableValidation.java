package pl.coddlers.core.services;

import org.springframework.data.domain.PageRequest;
import pl.coddlers.core.exceptions.WrongParametersException;

public class PageableValidation {
	private final static int MAX_RESULT = 100;
	private Integer page;
	private Integer size;

	PageableValidation(Integer page, Integer size) {
		this.page = page;
		this.size = size;
		validate();
	}

	private void validate() {
		if (this.page == null) {
			this.page = 1;
		}
		if (this.size == null) {
			this.size = MAX_RESULT;
		}
		if (page < 1) {
			throw new WrongParametersException("page < 1 ");
		}
		if (size < 0) {
			throw new WrongParametersException("size < 0");
		}
	}

	PageRequest createPageRequest() {
		return PageRequest.of(get0BasedIndexedPage(), size);
	}

	private int get0BasedIndexedPage() {
		return page - 1;
	}

}
