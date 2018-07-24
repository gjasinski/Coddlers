export class User {
  private _userMail: string;
  private _password: string;
  private _firstname: string;
  private _lastname: string;
  private _userRoles: string[];

  constructor(userMail: string, password: string, firstname: string, lastname: string, userRoles: string[]) {
    this._userMail = userMail;
    this._password = password;
    this._firstname = firstname;
    this._lastname = lastname;
    this._userRoles = userRoles;
  }

  get userMail(): string {
    return this._userMail;
  }

  get password(): string {
    return this._password;
  }

  get firstname(): string {
    return this._firstname;
  }

  get lastname(): string {
    return this._lastname;
  }

  get userRoles(): string[] {
    return this._userRoles;
  }

  public static fromJSON(jsonObj: any): User {
    return new User(jsonObj['userMail'], null, jsonObj['firstname'], jsonObj.lastname['lastname'],
      jsonObj['userRoles']);
  }

  public toJSON(): string {
    return JSON.stringify({
      userMail: this.userMail,
      password: this.password,
      firstname: this.firstname,
      lastname: this.lastname,
      userRoles: this.userRoles
    });
  }
}
