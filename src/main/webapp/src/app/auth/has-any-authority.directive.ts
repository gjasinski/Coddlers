import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {PrincipalService} from "./principal.service";

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *codHasAnyAuthority="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *codHasAnyAuthority="['ROLE_ADMIN', 'ROLE_USER']">...</some-element>
 * ```
 */
@Directive({
  selector: '[codHasAnyAuthority]'
})
export class HasAnyAuthorityDirective {

  private authorities: string[];

  constructor(private principal: PrincipalService,
              private templateRef: TemplateRef<any>,
              private viewContainerRef: ViewContainerRef) {}

  @Input()
  set codHasAnyAuthority(value: string|string[]) {
    this.authorities = typeof value === 'string' ? [ <string> value ] : <string[]> value;
    this.updateView();
    // Get notified each time authentication state changes.
    this.principal.getAuthenticationState().subscribe(() => this.updateView());
  }

  private updateView(): void {
    this.principal.hasAnyAuthority(this.authorities).then((result) => {
      this.viewContainerRef.clear();
      if (result) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      }
    });
  }

}
