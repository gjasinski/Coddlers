import {Component, OnInit} from '@angular/core';
import {CourseEditionService} from "../../../services/course-edition.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SubscriptionManager} from "../../../utils/SubscriptionManager";

@Component({
  selector: 'cod-invite-page',
  templateUrl: './invite-page.component.html',
  styleUrls: ['./invite-page.component.scss']
})
export class InvitePageComponent implements OnInit {
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private invitationLink: string;

  constructor(private editionService: CourseEditionService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    let paramsSub = this.route.queryParamMap.subscribe(params => {
      this.invitationLink = params.get('courseEdition');
    });
    this.subscriptionManager.add(paramsSub);
    this.editionService.addToCourseEdition(this.invitationLink);
    this.router.navigate([""]);
  }

}
