2018-4-25 qiangang
需要调用的函数:
1. 陈列采集
	1.1 货架号输入后,显示修改时间, 品种和层数:
	开始(增加):
		in: hjbh,scbh
		SQL:
			select hjqy, hjlx from hj_bh_sc where scbh=:as_scbh and hjbh=:as_hjbh;
			select max(xgrq) xgrq, count(distinct ch) ch_cnt, count(1) cnt from hj_sp_sc a where scbh=:as_scbh and hjbh=:as_hjbh;

			select * from dm_code where app='HJ' and usefor in ('HJLX','HJQY');

	清空:
		in:hjbh,scbh
		out: OK, or error

		delete from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh;

2. 货架查询:
汇总的:
	时间: 近10天g
		select * from (
			select trunc(xgrq) xgrq, count(*) cnt from hj_bh_sc where scbh=:as_scbh	group by trunc(xgrq) order by 1 desc) where rownuM<=10;
		明细:
		select  hjbh, xgrq,(select count(*) from hj_sp_sc b  where a.scbh=b.scbh and a.hjbh=b.hjbh) cnt
			from hj_bh_sc a
			where scbh=:as_scbh and xgrq>=:ad_rq and xgrq<:ad_rq+1 order by xgrq desc;

	货架排序: 汇总
		select substr(hjbh,1,2) hjbh2, count(*) cnt from hj_bh_sc where scbh=:as_scbh	group by substr(hjbh,1,2) order by 1;
		明细
		select  hjbh, xgrq,(select count(*) from hj_sp_sc b  where a.scbh=b.scbh and a.hjbh=b.hjbh) cnt
			from hj_bh_sc a
		where scbh=:as_scbh and substr(hjbh,1,2)=:as_hjbh2;


	货架明细:传入的日期, 或组别,显示货架明细:货架号,层数,品种, 主组别(SQL见上)

	点击[开始],增加货架数据:
		insert into hj_bh_sc (scbh,hjbh,hjqy,hjlx) vaules (:as_scbh, :as_hjbh, :as_hyqy, :as_hjlx);
		commit;

3. 陈列采集-商品:
	品种追加:
		传入条码(或管理码),调用查询接口, 无误后,调用增加接口. scbh,ch,wz,glbh. 返回流水号. 如果有误则不处理,返回-1或其他错误信息.
		--判断上一个品种是否相同， 相同则不处理
		select count(*) into :ll_find from hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and ch=:as_ch and wz=to_char(to_number('343' ) - 1);
		--生成流水号(因为门店组关系,与scbh结合才具有唯一性.
		select hj_sp_sc_seq.nextval into :al_sp_seq from dual;
		insert into hj_sp_sc (scbh,hjbh, ch,wz,glbh,spbh,lsh) values
			(:as_scbh,:as_hjbh,:as_ch, :as_wz,:as_glbh,:as_spbh,:al_sp_seq);
		--处理后wz+1
	品种查询:
		显示一节货架一层中原有品种
		in: scbh,hjbh, ch
		out: wz,spmc, spbh,lsh(不显示)

		call p_scbh(as_scbh);
		--返回位置,管理码(不显示),名称,会员价,流水号(不显示)
		select a.wz,a.glbh,spbh,spmc,jzhyj,a.lsh from hj_sp_sc a, user_xt_spda b
			where a.scbh=:as_scbh and a.hjbh=:as_hjbh and a.ch = :as_ch
		 		and a.glbh=b.glbh   order by wz;


	品种删除:
		in: scbh,hjbh(供校验), lsh
		out: OK, or error
		delete hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and lsh=:al_lsh;

	按层删除:
		in: scbh, hjbh, ch
		out: OK, or error
		--循环处理
		delete hj_sp_sc where scbh=:as_scbh and hjbh=:as_hjbh and lsh=:al_lsh;

4. 陈列采集-商品-定位
	先查询出对应的管理码,如果是13位条码,则直接查询.(现不存在, 有库存称重商品条码7位数,与管理码相同的情况)
	查询:
		in:传入管理码(条码),
		out1: 商品名称,价格
		out2: 货架号,位置 . 要求光标定位在当前货架号,如果有

	call p_scbh(:as_scbh);
	select spmc,jzsj,jzhyj from user_xt_spda where glbh=:as_glbh;
	--位置输出. 如果货架号为空则全部货架查询
	select hjbh,ch,wz from hj_sp_sc
		where scbh=:as_scbh and glbh=:as_glbh and (hjbh=:as_hjbh or :as_hjbh is null );


表结构
货架
create table hj_bh_sc (
scbh  char(5),
HJBH  VARCHAR2(10) NOT NULL,
hjqy  varchar2(2),
hjlx   varchar2(2),
xgrq  date);

alter table hj_bh_sc add constraint hj_bh_sc_pk primary key (scbh,hjbh);

COMMENT   ON   TABLE   hj_bh_sc   IS   '商场采集的货架编号';
COMMENT   ON   COLUMN   hj_bh_sc.hjbh   IS   '货架编号';
COMMENT   ON   COLUMN   hj_bh_sc.hjqy   IS   '货架区域,Y的打标价签:HJ货架Y,DW堆位Y,DJ端架Y,CK1仓库1,CK2仓库2';
COMMENT   ON   COLUMN   hj_bh_sc.hjlx   IS   '货架陈列类型:CB层板,WL网篮,GG挂钩,CX促销位,LF冷风,BG冰柜';

货架商品
create table hj_sp_sc(
scbh char(5),
hjbh varchar2(10),
CH  VARCHAR2(4)  NOT NULL,
WZ  VARCHAR2(3)  NOT NULL,
glbh char(7) not null,
xgrq date,
lsh number);
COMMENT   ON   TABLE   hj_sp_sc   IS   '商场采集的商品陈列';
COMMENT   ON   COLUMN   hj_sp_sc.hjbh   IS   '货架编号';
COMMENT   ON   COLUMN   hj_sp_sc.CH   IS   '层号';
COMMENT   ON   COLUMN   hj_sp_sc.WZ   IS   '位置';
COMMENT   ON   COLUMN   hj_sp_sc.xgrq   IS   '修改日期';
COMMENT   ON   COLUMN   hj_sp_sc.lsh   IS   '流水号';
alter table hj_sp_sc add constraint hj_sp_sc_pk  primary key (scbh,lsh);

--日志表:dml: I insert, D delete, U修改前值. 触发器产生
create table rz_hj_bh_sc as select * from hj_bh_sc;
alter table rz_hj_bh_sc add dml char(1);

create table rz_hj_sp_sc as select * from hj_sp_sc;
alter table rz_hj_sp_sc add dml char(1);
