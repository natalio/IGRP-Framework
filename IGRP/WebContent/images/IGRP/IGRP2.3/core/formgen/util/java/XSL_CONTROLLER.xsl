<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xdb="http://xmlns.oracle.com/xdb" >

    <xsl:output method="text" encoding="UTF-8" indent="no" />
    <xsl:preserve-space elements="*"/>

    <!-- Join all templates to create controller -->
    <xsl:template name="create-controller">
    	<xsl:value-of select="$newline"/>
     	<xsl:call-template name="import-packages-controller"></xsl:call-template>
     	<xsl:value-of select="$newline"/>
 		<xsl:value-of select="concat('public class ',$class_name,'Controller extends Controller {')"/>
	 		<xsl:value-of select="$tab2"/>
	     	<xsl:value-of select="$newline"/>
	 		<xsl:call-template name="actionIndex"></xsl:call-template>
	 		<xsl:value-of select="$newline"/>
	 		 <xsl:call-template name="createActions"></xsl:call-template>
	 		<xsl:value-of select="$newline"/>
 		<xsl:value-of select="'}'"/>
    </xsl:template>

     <!-- import all class to using in controller -->
 	<xsl:template name="import-packages-controller">
 		<xsl:value-of select="concat('package ',$package_name)"/>
		<xsl:value-of select="$newline"/>
 		<xsl:value-of select="$import_controller"/>
		<xsl:value-of select="$newline"/>
 		<xsl:value-of select="$import_exception"/>
		<xsl:value-of select="$newline"/>
		<xsl:call-template name="import-class-models"></xsl:call-template>
		<xsl:value-of select="$newline"/>
 	</xsl:template>
	
	<xsl:template name="import-class-models">
		<xsl:if test="(count(/rows/content/*[@type = 'toolsbar']) &gt; 0) or (count(/rows/content//tools-bar) &gt; 0)">
           <xsl:for-each select="/rows/content/*[@type = 'toolsbar']/item">   <!-- Button in tools-bar -->
          	  	<xsl:if test="not(./page=preceding::node()/./page) and ./page!=$class_name and ./target!='_self'">
          	  		<xsl:call-template name="gen-import-model"><xsl:with-param name="page__"><xsl:value-of select="./page"/> </xsl:with-param></xsl:call-template>
           		</xsl:if>
           </xsl:for-each>
           <xsl:for-each select="//tools-bar/item">   <!-- Button in form -->
         		<xsl:if test="not(./page=preceding::node()/./page) and ./page!=$class_name and ./target!='_self'">
          	  		<xsl:call-template name="gen-import-model"><xsl:with-param name="page__"><xsl:value-of select="./page"/> </xsl:with-param></xsl:call-template>
           		</xsl:if>
           </xsl:for-each>           
           <xsl:for-each select="//context-menu/item">   <!-- Button in table -->
            <xsl:if test="not(@rel=preceding::node()/@rel) and not(./page=preceding::node()/./page) and $class_name!=@rel and ./target!='_self'">
	          	<xsl:call-template name="gen-import-model"><xsl:with-param name="page__"><xsl:value-of select="./page"/> </xsl:with-param></xsl:call-template>
            </xsl:if>
           </xsl:for-each>
        </xsl:if>
	</xsl:template>
	
	<xsl:template name="gen-import-model">
		<xsl:param name="page__"></xsl:param>
		<xsl:variable name="pacak_import">
			<xsl:call-template name="lowerCase">
	    		<xsl:with-param name="text">
	    			<xsl:value-of select="$page__"></xsl:value-of>
	    		</xsl:with-param>
	    	</xsl:call-template>
		</xsl:variable>	
		<xsl:if test="$page__ != ''">
			<xsl:value-of select="concat($package_import_name,'.',$pacak_import,'.*',';')"/>
			<xsl:value-of select="$newline"/>	
		</xsl:if>
	</xsl:template>
	<!-- create actions based in button -->
	<xsl:template name="createActions">
		 <xsl:if test="(count(/rows/content/*[@type = 'toolsbar']) &gt; 0) or (count(/rows/content//tools-bar) &gt; 0)">
           <xsl:for-each select="/rows/content/*[@type = 'toolsbar']/item">   <!-- Button in tools-bar -->
          	<xsl:call-template name="actions">
				<xsl:with-param name="page_"><xsl:value-of select="./page"/></xsl:with-param>
				<xsl:with-param name="app_"><xsl:value-of select="./app"/></xsl:with-param>
				<xsl:with-param name="link_"><xsl:value-of select="./link"/></xsl:with-param>
				<xsl:with-param name="title_"><xsl:value-of select="@rel"/></xsl:with-param>
				<xsl:with-param name="target_"><xsl:value-of select="./target"/></xsl:with-param>
            </xsl:call-template>
           </xsl:for-each>
           <xsl:for-each select="//tools-bar/item">   <!-- Button in form -->
          	<xsl:call-template name="actions">
				<xsl:with-param name="page_"><xsl:value-of select="./page"/></xsl:with-param>
				<xsl:with-param name="app_"><xsl:value-of select="./app"/></xsl:with-param>
				<xsl:with-param name="link_"><xsl:value-of select="./link"/></xsl:with-param>
				<xsl:with-param name="title_"><xsl:value-of select="@rel"/></xsl:with-param>
				<xsl:with-param name="target_"><xsl:value-of select="./target"/></xsl:with-param>
            </xsl:call-template>
           </xsl:for-each>           
           <xsl:for-each select="//context-menu/item">   <!-- Button in table -->
            <xsl:if test="not(@rel=preceding::node()/@rel)">
	          	<xsl:call-template name="actions">
					<xsl:with-param name="page_"><xsl:value-of select="./page"/></xsl:with-param>
					<xsl:with-param name="app_"><xsl:value-of select="./app"/></xsl:with-param>
					<xsl:with-param name="link_"><xsl:value-of select="./link"/></xsl:with-param>
					<xsl:with-param name="title_"><xsl:value-of select="@rel"/></xsl:with-param>
					<xsl:with-param name="target_"><xsl:value-of select="./target"/></xsl:with-param>
	            </xsl:call-template>
            </xsl:if>
           </xsl:for-each>
        </xsl:if>
	</xsl:template>
	
	<xsl:template name="actions">
		<xsl:param name="page_"/>
		<xsl:param name="app_"/>
		<xsl:param name="target_"/>
		<xsl:param name="link_"/>
		<xsl:param name="title_"/>
		
		<xsl:choose>
			<xsl:when test="$target_='submit' or $target_='submit_ajax'">
				<xsl:call-template name="gen-action">
					<xsl:with-param name="action_name_"><xsl:value-of select="$title_"/></xsl:with-param>
					<xsl:with-param name="page_"><xsl:value-of select="$page_"/></xsl:with-param>
					<xsl:with-param name="app_"><xsl:value-of select="$app_"/></xsl:with-param>
					<xsl:with-param name="link_"><xsl:value-of select="$link_"/></xsl:with-param>
					<xsl:with-param name="type_render_"><xsl:value-of select="'render'"/></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="gen-action">
					<xsl:with-param name="action_name_"><xsl:value-of select="$title_"/></xsl:with-param>
					<xsl:with-param name="page_"><xsl:value-of select="$page_"/></xsl:with-param>
					<xsl:with-param name="app_"><xsl:value-of select="$app_"/></xsl:with-param>
					<xsl:with-param name="link_"><xsl:value-of select="$link_"/></xsl:with-param>
					<xsl:with-param name="type_render_"><xsl:value-of select="'redirect'"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="$newline"/>
		<xsl:value-of select="$tab"/>
	</xsl:template>
	
	<xsl:template name="gen-action">
		<xsl:param name="action_name_"/>
		<xsl:param name="page_" select="''"/>
		<xsl:param name="app_" select="''"/>
		<xsl:param name="link_" select="''"/>
		<xsl:param name="type_render_"/>
		
		<xsl:variable name="model">
   			<xsl:value-of select="$page_"></xsl:value-of>
		</xsl:variable>
			
		<xsl:variable name="action">
			<xsl:call-template name="CamelCaseWord">
	    		<xsl:with-param name="text">
	    			<xsl:value-of select="$action_name_"></xsl:value-of>
	    		</xsl:with-param>
	    	</xsl:call-template>
		</xsl:variable>			
		<xsl:variable name="app__">
			<xsl:call-template name="lowerCase">
	    		<xsl:with-param name="text">
	    			<xsl:value-of select="$app_"></xsl:value-of>
	    		</xsl:with-param>
	    	</xsl:call-template>
		</xsl:variable>			
		
		<xsl:variable name="link__">
			<xsl:call-template name="lowerCase">
	    		<xsl:with-param name="text">
	    			<xsl:value-of select="$link_"></xsl:value-of>
	    		</xsl:with-param>
	    	</xsl:call-template>
		</xsl:variable>
		<xsl:value-of select="$newline"/>
		<xsl:value-of select="$tab"/>
     	<xsl:value-of select="concat('public void action',$action,'() throws IOException{')"/>
		<xsl:value-of select="$newline"/>
		<xsl:value-of select="$tab2"/>
		<xsl:if test="$page_ != ''">
			<xsl:choose>
				<xsl:when test="$type_render_='render'">
					<xsl:value-of select="concat($model,' model = new ',$model,'();')"/>
					<xsl:value-of select="$newline"/>
					<xsl:value-of select="$tab2"/>
					<xsl:value-of select="concat($model,'View',' view = new ',$model,'View(model);')"/>
					<xsl:value-of select="$newline"/>
					<xsl:value-of select="$tab2"/>
					<xsl:value-of select="'this.renderView(view);'"/>
				</xsl:when>
				<xsl:when test="$type_render_='redirect'">
					<xsl:value-of select="$tab"/>
					<xsl:value-of select="concat('this.redirect(',$double_quotes,$app__,$double_quotes,',',$double_quotes,$page_,$double_quotes,',',$double_quotes,$link__,$double_quotes,');')"/>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
		<xsl:value-of select="$newline"/>
		<xsl:value-of select="$tab"/>
 		<xsl:value-of select="'}'"/>
	</xsl:template>
	
 	<!-- add actionIndex() - it is default method in any controller -->
	<xsl:template name="actionIndex">
		<xsl:call-template name="gen-action">
			<xsl:with-param name="action_name_"><xsl:value-of select="'index'"/></xsl:with-param>
			<xsl:with-param name="page_"><xsl:value-of select="$class_name"/></xsl:with-param>
			<xsl:with-param name="type_render_"><xsl:value-of select="'render'"/></xsl:with-param>
		</xsl:call-template>
 	</xsl:template>

</xsl:stylesheet>