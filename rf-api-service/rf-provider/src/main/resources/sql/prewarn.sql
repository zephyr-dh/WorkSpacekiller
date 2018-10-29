界面： 保质期预警
生成日期  品种数  已检查数  组别范围
汇总查询
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('hzcx','00003',:cur_result);

界面： 保质期预警单生成
展示组别
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjdsc_fzm','00003',:cur_result);

fzlx一样的是同个部门，勾选了 deptlevelid 为0的，fzlx一样的一起勾选；


生成预警单  界面：保质期预警单生成  按钮：预警单生成
传数组：fzm给后台
pkg_goodsoutoftime.p_goodsoutoftime('00003',:cur_result);

declare
c VARTABLETYPE;
begin
  select fzm bulk collect into c from dm_fzm where fzm = 50;
	pkg_goodsoutoftime.p_goodsoutoftime('00003',c);
end;


点击某个保质期预警单  界面：保质期预警--商品统计
按组汇总信息
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjd_jc_fzm','00003',:cur_result,to_date('2018-05-11 10:26:04','yyyy-mm-dd hh24:mi:ss'));




点击某个保质期预警单某个组  界面：保质期预警--商品选择
组别对应的商品列表
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjd_jc_sp','00003',:cur_result,to_date('2018-05-11 10:26:04','yyyy-mm-dd hh24:mi:ss'),50);




界面：保质期预警--商品检查

获取货架号
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjd_sp_hjbh','00003',:cur_result,null,null, '5110712' );
获取生成日期
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjd_sp_scrq','00003',:cur_result,null,null, '5110402’ );


扫描商品条码  界面：保质期预警--商品检查
显示单品信息
var cur_result refcursor;
exec pkg_goodsoutoftime.p_goodsoutoftime_cx('yjd_dpjc','00003',:cur_result,null,null, '6901209212215' );


预警检查  保质期预警--商品检查
exec pkg_goodsoutoftime.p_goodsoutoftime_cz('update','00003', '5110712', date'2017-02-01',as_hjbh  varchar2,'处理人员',‘预警单生成日期’ );
exec pkg_goodsoutoftime.p_goodsoutoftime_cz('insert','00003', '5110712', date'2017-02-01',as_hjbh  varchar2,'处理人员',‘预警单生成日期’ );
exec pkg_goodsoutoftime.p_goodsoutoftime_cz('delete','00003', '5110712', date'2017-02-01',as_hjbh  varchar2,'处理人员',‘预警单生成日期’ );

