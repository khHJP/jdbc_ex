create table MEMBER_DEL
as
select 
    m.*,
    systimestamp DEL_DATE
from member m
where 1 = 0;


create or replace trigger trig_tbl_member_del
    before 
    delete on member
    for each row
begin
    insert into
        member_del
        values(:old.id, :old.name, :old.gender, :old.birthday, :old.email, :old.point, :old.reg_date, systimestamp);
end;
/
