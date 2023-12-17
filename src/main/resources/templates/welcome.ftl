<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<style>html, body {
        padding: 0;
        margin: 0;
    }</style>
<div style="font-family:Arial,Helvetica,sans-serif; line-height: 1.5; font-weight: normal; font-size: 15px; color: #2F3044; min-height: 100%; margin:0; padding:0; width:100%; background-color:#edf2f7">
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%"
           style="border-collapse:collapse;margin:0 auto; padding:0; max-width:600px">
        <tbody>
        <tr>
            <td align="center" valign="center" style="text-align:center; padding: 40px">
                <a href="${link}" rel="noopener" target="_blank">
                    <img alt="Logo" src="http://mat-data.com/media/logos/demo2-sticky.png"/>
                </a>
            </td>
        </tr>
        <tr>
            <td align="left" valign="center">
                <div style="text-align:left; margin: 0 20px; padding: 40px; background-color:#ffffff; border-radius: 6px">
                    <!--begin:Email content-->
                    <div style="padding-bottom: 30px; font-size: 17px;">
                        <strong>${welcome}</strong>
                    </div>
                    <div style="padding-bottom: 30px">${message1}</div>
                    <div style="padding-bottom: 30px">${username}
                        <br/>${password}</div>
                    <div style="padding-bottom: 30px">${message2}
                        <br/>
                        <a href="${link}" rel="noopener" target="_blank"
                           style="text-decoration:none;color: #009ef7">${link}</a></div>
                    <!--end:Email content-->
                    <div style="padding-bottom: 10px">${bestRegards}
                        <br>Mat-Data Team.
                        <tr>
                            <td align="center" valign="center"
                                style="font-size: 13px; text-align:center;padding: 20px; color: #6d6e7c;">
                                <p>Copyright &copy;
                                    <a href="${link}" rel="noopener" target="_blank">mat-data</a>.</p>
                            </td>
                        </tr>
                        <br/></div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>