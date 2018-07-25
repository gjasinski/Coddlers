import {AbstractControl} from '@angular/forms';

export class PasswordValidation {

  static MatchPassword(AC: AbstractControl) {
    let password = AC.get('password').value;
    let confirmPassword = AC.get('passwordRepeat').value;

    if(password !== confirmPassword) {
      AC.get('passwordRepeat').setErrors( {MatchPassword: true} );
    } else {
      return null;
    }
  }
}
