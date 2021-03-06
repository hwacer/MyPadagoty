<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>新增标签</title>
<!-- BOOTSTRAP STYLES-->
<link href="assets/css/bootstrap.css" rel="stylesheet" />
<!-- FONTAWESOME STYLES-->
<link href="assets/css/font-awesome.css" rel="stylesheet" />
<!-- PAGE LEVEL STYLES -->
<link href="assets/css/bootstrap-fileupload.min.css" rel="stylesheet" />
<!--CUSTOM BASIC STYLES-->
<link href="assets/css/basic.css" rel="stylesheet" />
<!--CUSTOM MAIN STYLES-->
<link href="assets/css/custom.css" rel="stylesheet" />
<!-- GOOGLE FONTS-->
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css' />
<!-- 多图上传 -->
<link href="assets/css/fileinput.min.css" rel="stylesheet" />	

</head>
<body>
	<div id="wrapper">
		<!-- /. NAV TOP   -->
		<jsp:include page="leftTopMenu.jsp" />
		<!-- /. NAV SIDE  -->
		<div id="page-wrapper">
			<div id="page-inner">
				<div class="row">
					<div class="col-md-12">
						<h1 class="page-head-line">新增标签</h1>
					</div>
				</div>
				<!-- /. ROW  -->
				<div class="row">
					<div class="col-md-6 col-sm-6 col-xs-12">
						<div class="panel panel-info">

							<div class="panel-body">
								<form role="form"
									<c:choose>
										<c:when test="${!empty keyword}">action="keyword/update"</c:when>
										<c:otherwise>action="keyword/add"</c:otherwise>
									</c:choose>
									method="POST" Enctype="multipart/form-data">
									<input type="hidden" name="keyId" value="${keyword.keyId }">
									<div class="form-group">
										<label>标签名</label> <input class="form-control" type="text"
											name="name"
											value='<c:if test="${!empty keyword}"> ${keyword.name }</c:if>'>
									</div>
									<div class="form-group">
										<label class="row col-md-10">图标</label>
										<div class="row col-md-10">
											<div class="fileupload fileupload-new"
												data-provides="fileupload">
												<div class="fileupload-new thumbnail"
													style="width: 150px; height: 100px;">
													<c:if test="${!empty keyword}"><img src="${keyword.iconUrl }" alt="${keyword.name }" /></c:if>
													<c:if test="${empty keyword}"><img src="assets/img/demoUpload.jpg" alt="标签logo" /></c:if>
												</div>
												<div class="fileupload-preview fileupload-exists thumbnail"
													style="max-width: 150px; max-height: 100px; line-height: 20px;"></div>
												<div>
													<span class="btn btn-file btn-primary"><span
														class="fileupload-new">Select image</span><span
														class="fileupload-exists">Change</span><input type="file"
														name="iconPic" id="iconPic"></span> <a href="#"
														class="btn btn-danger fileupload-exists"
														data-dismiss="fileupload">Remove</a>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="row col-md-10">描述</label>
										<textarea class="form-control text-left" rows="3"
											name="description"><c:if test="${!empty keyword}"> ${keyword.description }</c:if></textarea>
									</div>
									
									<button type="submit" class="btn btn-info">保存</button>

								</form>
							</div>
						</div>
					</div>

				</div>

			</div>
			<!-- /. PAGE INNER  -->
		</div>
		<!-- /. PAGE WRAPPER  -->
	</div>
	<!-- /. WRAPPER  -->
	<jsp:include page="footer.jsp" />
	<!-- /. FOOTER  -->

	<!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
	<!-- JQUERY SCRIPTS -->
	<script src="assets/js/jquery-1.10.2.js"></script>
	<!-- BOOTSTRAP SCRIPTS -->
	<script src="assets/js/bootstrap.js"></script>
	<!-- PAGE LEVEL SCRIPTS -->
	<script src="assets/js/bootstrap-fileupload.js"></script>
	<!-- METISMENU SCRIPTS -->
	<script src="assets/js/jquery.metisMenu.js"></script>
	<!-- CUSTOM SCRIPTS -->
	<script src="assets/js/custom.js"></script>

	<!-- 多图上传 -->
	<script src="assets/js/fileinput.min.js"></script>	
</body>
</html>