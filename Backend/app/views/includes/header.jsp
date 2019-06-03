<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="authors" content="Jéremie Chatillon, James Smith, Antoine Rochat et Benoit Schopfer">

  <base href="${pageContext.request.contextPath}/">
  <title>${pageTitle}</title>

  <!-- Bootstrap Core CSS -->
  <link href="static/vendor/bootstrap/css/bootstrap.css" rel="stylesheet">

  <!-- MetisMenu CSS -->
  <link href="static/vendor/metisMenu/metisMenu.css" rel="stylesheet">

  <!-- Custom CSS -->
  <link href="static/dist/css/sb-admin-2.css" rel="stylesheet">

  <!-- Morris Charts CSS -->
  <link href="static/vendor/morrisjs/morris.css" rel="stylesheet">

  <!-- Custom Fonts -->
  <link href="static/vendor/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></link>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>

  <!-- remove from cache-->

  <![endif]-->

</head>

<body>

<div id="wrapper">

  <!-- Navigation -->
  <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="index.html">AMT Gamification</a>
    </div>
    <!-- /.navbar-header -->

    <ul class="nav navbar-top-links navbar-right">

      <!-- /.dropdown -->
      <li class="dropdown">
        <a id="dropdown-toggle" class="dropdown-toggle" data-toggle="dropdown" href="#">
          <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
        </a>
        <ul class="dropdown-menu dropdown-user">
          <li><a><i class="fa fa-user fa-fw"></i>${principal}</a></li>
          <li class="divider"></li>
          <li><a href="pages/userSettings"><i class="fa fa-cog fa-fw"></i> User Profile</a>
          </li>
          <li class="divider"></li>
          <li><a id="bLogoutLocator" href="./auth?action=logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
          </li>
        </ul>
        <!-- /.dropdown-user -->
      </li>
      <!-- /.dropdown -->
    </ul>
    <!-- /.navbar-top-links -->

    <div class="navbar-default sidebar" role="navigation">
      <div class="sidebar-nav navbar-collapse">
        <ul class="nav" id="side-menu">
          <li>
            <a id="bDashboardSideBar" href="pages/applications"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
          </li>
          <c:if test="${status == 'ADMIN'}">
            <li>
              <a href="pages/usersManager"><i class="fa fa-user fa-fw"></i> Users Manager</a></a>
            </li>
          </c:if>
        </ul>
      </div>
      <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
  </nav>