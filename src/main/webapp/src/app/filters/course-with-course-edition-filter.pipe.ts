import {Pipe, PipeTransform} from '@angular/core';
import {CourseWithCourseEdition} from "../models/courseWithCourseEdition";

@Pipe({
  name: 'courseWithCourseEditionFilter'
})
export class CourseWithCourseEditionFilterPipe implements PipeTransform {

  transform(items: CourseWithCourseEdition[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;

    searchText = searchText.toLowerCase();
    return items.filter(it => {
      return it.course.title.toLowerCase().includes(searchText);
    });
  }

}
