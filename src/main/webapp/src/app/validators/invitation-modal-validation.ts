import {FormControl} from "@angular/forms";
import {Email} from "../models/email";

export class InvitationModalValidation {

  static hasAt(control: FormControl) {
    if (!control.value.toString().includes('@')) {
      return {
        isProperEmail: true
      };
    }

    return null;
  }

  static localPartLength(control: FormControl) {
    const email = Email.parseMail(control.value.toString());
    if (email.local.length > 64 || email.local.length <= 0){
      return {
        invalidLengthLocal: true
      }
    }
    return null;
  }

  static domainPartLength(control: FormControl) {
    const email = Email.parseMail(control.value.toString());
    if (email.domain.length > 255 || email.domain.length <= 0){
      return {
        invalidLengthDomain: true
      }
    }
    return null;
  }

  static validators = [InvitationModalValidation.hasAt, InvitationModalValidation.localPartLength,
    InvitationModalValidation.domainPartLength];
}
