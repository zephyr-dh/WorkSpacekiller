---获取商品名称
select spmc,glbh from xt_spda  where glbh in (select glbh from xt_ftm  where spbh=as_spbh)

---统计

select count(*) ,cj_date from cj where cj_date>=trunc(sysdate) group by cj_date;

---提交
insert into cj (scname,czy,glbh,spbh,cj_date,price,cprice,bz) as ();