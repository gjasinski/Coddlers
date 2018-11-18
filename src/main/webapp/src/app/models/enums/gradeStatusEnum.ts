import {Enum, EnumValue} from 'ts-enums';

export class GradeEnumStatus extends EnumValue {
  constructor(name: string) {
    super(name);
  }

  public toString(){
    return this.description.toString().toUpperCase().replace(/[_]/g, " ");
  }
}

export class GradeStatus extends Enum<GradeEnumStatus> {

  NOT_GRADED: GradeEnumStatus = new GradeEnumStatus('NOT_GRADED');
  GRADED: GradeEnumStatus = new GradeEnumStatus('GRADED');

  constructor() {
    super();
    this.initEnum('GradeStatus');
  }

  public static getEnumFromString(str: string): GradeEnumStatus {
    for (const s of GradeStatusEnum.values) {
      if (s.propName.toLowerCase() === str.toLowerCase()) {
        return s;
      }
    }
    return null;
  }
}

export const GradeStatusEnum: GradeStatus = new GradeStatus();
