import { Pipe, PipeTransform } from '@angular/core';
import {Course} from "../models/course";

@Pipe({
  name: 'courseFilter'
})
export class CourseFilterPipe implements PipeTransform {

  transform(items: Course[], searchText: string): any[] {
    if(!items) return [];
    if(!searchText) return items;

    searchText = searchText.toLowerCase();
    return items.filter( it => {
      return it.title.toLowerCase().includes(searchText);
    });
  }

}
