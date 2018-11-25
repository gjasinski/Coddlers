export class Email {
  private _raw:string;
  private _local:string;
  private _domain: string;

  constructor(local: string, domain: string) {
    this._raw = `${local}@${domain}`;
    this._local = local;
    this._domain = domain;
  }

  get raw(): string {
    return this._raw;
  }

  get local(): string {
    return this._local;
  }

  get domain(): string {
    return this._domain;
  }

  public static fromJSON(jsonObj: any): Email {
    return new Email(jsonObj.local, jsonObj.domain);
  }

  public toJSON() {
    return {
      local: this.local,
      domain: this.domain
    }
  }

  public static parseMail(value: string): Email{
    const arr = value.split('@');
    return Email.fromJSON({raw: value, local: `${arr[0]}`, domain: `${arr[1]}`});
  }
}
