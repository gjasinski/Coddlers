export class User {
  private _userMail: string;
  private _password: string;
  private _firstname: string;
  private _lastname: string;

  constructor(
    userMail: string,
    password: string,
    firstname: string,
    lastname: string,
  ) {}

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

  public toJSON(): string {
    return JSON.stringify({
      userMail: this._userMail,
      password: this._password,
      firstname: this._firstname,
      lastname: this._lastname
    });
  }
}
