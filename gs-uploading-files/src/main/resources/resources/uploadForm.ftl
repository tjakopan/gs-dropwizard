<#-- @ftlvariable name="" type="resources.UploadFormView" -->
<html>
<body>
<#if message??>
  <div>
    <h2>${message}</h2>
  </div>
</#if>
<div>
  <form method="post" enctype="multipart/form-data" action="/">
    <table>
      <tr>
        <td>File to upload:</td>
        <td>
          <input type="file" name="file"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <input type="submit" value="Upload"/>
        </td>
      </tr>
    </table>
  </form>
</div>
<div>
  <ul>
      <#list files as file>
        <li>
          <a href="/files/${file}">${file}</a>
        </li>
      </#list>
  </ul>
</div>
</body>
</html>
