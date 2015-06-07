<!--
    https://github.com/charlesmudy/Responsive-HTML-Email-Template
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="format-detection" content="telephone=no" /> <!-- disable auto telephone linking in iOS -->
    <title>Revizor: ${header}</title>
</head>
<body bgcolor="#e7e7ef" leftmargin="10" marginwidth="10" topmargin="10" marginheight="10" offset="10" style="color: #36353a; font-family: Arial, sans-serif;">

        <h2 style="color: #445372;">${header}</h2>

        ${raw(message)}

</body>
</html>
