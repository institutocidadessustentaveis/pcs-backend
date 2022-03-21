<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
    <head>
 		<link rel="preconnect" href="https://fonts.gstatic.com">
 			<style type="text/css">
 				@font-face{
					 font-family:'Roboto';
					 font-style:normal;
					 font-weight:300;
					 font-display:swap;
					 src:url('https://fonts.gstatic.com/s/roboto/v27/KFOlCnqEu92Fr1MmSU5vAw.ttf') 
					 format('truetype')}
				@font-face{
					font-family:'Roboto Slab';
					font-style:normal;
					font-weight:300;
					font-display:swap;
					src:url('https://fonts.gstatic.com/s/robotoslab/v13/BngbUXZYTXPIvIBgJJSb6s3BzlRRfKOFbvjo0oSWaA.ttf') format('truetype')}
 				body{
					 margin:0;
					 padding:0;
					 min-width:100% !important;
					 font-family:'Helvetica',sans-serif}
 				@media only screen and (min-device-width: 901px){.content{width:900px !important}}
 			</style>
 	</head>
 		<body yahoo style="background-color: white; min-width: 100% !important; font-family: 'Roboto', sans-serif; margin: 0; padding: 0;">
		<div>
			<div alt style="border:0; background: url('${urlAPI}/arquivo/imagem/999978') no-repeat center center / cover; padding: 0px 0; height: 107px; width: 100%; background-size: 100%;"></div>
		</div>
		<br>
		<div style='margin: 30px 30px'>
 		<table class="content" cellpadding="0" cellspacing="0" align="center" style="border:0; width: min-content; max-width: 900px; margin-top: 32px;">
 			<tr>
 				<td>
 					<table align="center" class="h1" style="font-size: 24px; font-weight: bold; color: #00C300; font-family: sans-serif; padding: 0 0 32px;">
 						<tr>
 							<td>${titulo}</td>
         				</tr>
         			</table>
         		</td>
         	</tr>
         	<tr>
         		<td>
         			<table width="850" class="text" style="font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif; padding: 0 0 24px;">
         				<tr>
         					<td>${textoIntroducao}</td>
                 		</tr>
                 	</table>
                </td>
          </tr>
          <tr>
         		<td class="header">
         			<table width="425" align="left" style="border:0" cellpadding="0" cellspacing="0">
         				<tr>
         					<td height="425px">
								<#if imagemPrimeiroBanner??> 
                                        <img src='${imagemPrimeiroBanner.extensao},${imagemPrimeiroBanner.conteudo}' width="425" height="425px" border="0" alt="img">
								</#if>
							</td>
 						</tr>
 					</table>
 					<table class="col425" align="left" width="425" cellpadding="0" cellspacing="0" style="background-color: #e3dbd2; border:0; max-width: 425px; width: 425 !important; height: 425px;">
 						<tr>
 							<td height="425px">
 								<table height="425px" style="padding: 50px; border: 0" width="100%" cellspacing="0" cellpadding="0">
 									<tr>
										<td>
 											<p class="h2" style="font-size: 18px; font-weight: bold; margin-bottom: -16px; text-transform: uppercase; color: black; font-family: sans-serif;"> ${tituloPrimeiroBanner}</p>
         									<p class="text" style="margin-top: 32; font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;"> ${textoPrimeiroBanner}</p>
                 							<p align="center" style="margin-top: 32; text-decoration: none; display: block; -webkit-border-radius: 12px; color: black; text-transform: uppercase; padding: 16px; font-size: 12px; border: 2px solid black;" class="botao">
    											<a href="${textoBotao01}">CONFIRA</a>		 
								            </p>
 										</td>
 									</tr>
 								</table>
 							</td>
 						</tr>
 					</table>
 				</td>
 		</tr>
 		<tr>
 			<td class="header">
 				<table width="405" align="left" style="border:0; margin-top: 64px; margin-right: 20px;" cellpadding="0" cellspacing="0">
 					<tr>
 						<td height="300px" class="caixa-chamada" style="border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;">
 							<p class="subtitulo" width="425" height="425px" style="font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;"> ${tituloChamada01}</p>
 							<p class="h2" width="425" height="425px" style="font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;"> ${subtituloChamada01}</p>
 							<p class="text" width="425" height="425px" style="font-size: 14px; line-height: 21px; color: black; font-family: Roboto Slab, serif;"> ${textoChamada01}</p>
                 		</td>
                 	</tr>
             	</table>
             	<table width="405" align="left" style="border: 0; margin-top: 64px; margin-left: 20px;" cellpadding="0" cellspacing="0">
             		<tr>
             			<td height="300" class="caixa-chamada" style="border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;">
            				<p class="subtitulo" width="425" height="425px" style="font-size: 14px; line-height: 21px; text-transform: uppercase; color: black; font-weight: bold;"> ${tituloChamada02}</p>
                     		<p class="h2" width="425px" height="425" style="font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;"> ${subtituloChamada02}</p>
                     		<p class="text" width="425px" height="425" style="font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;"> ${textoChamada02}</p>
            			</td>
             		</tr>
             	</table>
 			</td>
 		</tr>
 		<tr>
 			<td class="header">
 				<table align="left" style="border: 0; margin-top: 64px; margin-right: 20px;" width="850px" cellpadding="0" cellspacing="0">
 					<tr>
 						<td style="padding: 0 10px 0 0;" height="425px">
                        <td style="padding: 0 10px 0 0;" height="425px">
							<#if imagemPrincipal??> 
                                <div align="center" class="imagem-principal" width="850" height="500px" alt="" style="border: 0; color: #fff; background: url('${urlAPI}/newsletter/imagem/${imagemPrincipal.id}') no-repeat center center / cover; padding: 150px 0;">
							</#if>
                                <div align="center" class="texto-imagem-principal" style="font-size: 18px; font-weight: bold; text-transform: uppercase; color: white;"> ${tituloImagemPrincipal}</div>
							
							<div align="center" style="padding: 15px;"> ${legendaImagemPrincipal}</div>
						</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="header">
				<table align="left" width="405" style="border: 0; margin-top: 64px; margin-right: 20px;" cellpadding="0" cellspacing="0">
					<tr>
						<td height="300px" class="caixa-chamada" style="border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;">
							<p class="subtitulo" width="425px" height="425px" style="font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;"> ${tituloChamada03}</p>
							<p class="h2" width="425px" height="425px" style="font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;"> ${subtituloChamada03}</p>
							<p class="text" width="425px" height="425px" style="font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;"> ${textoChamada03}</p>
						</td>
					</tr>
				</table>
				<table align="left" width="405" style="border: 0; margin-top: 64px; margin-left: 20px;" cellpadding="0" cellspacing="0">
					<tr>
						<td height="300px" class="caixa-chamada" style="border-top-width: 4px; border-top-color: #de7921; border-top-style: solid; border-bottom-width: 4px; border-bottom-color: #de7921; border-bottom-style: solid; padding: 20px;">
							<p class="subtitulo" width="425" height="425px" style="font-size: 14px; color: black; line-height: 21px; text-transform: uppercase; font-weight: bold;"> ${tituloChamada04}</p>
							<p class="h2" width="425px" height="425px" style="font-size: 18px; color: black; font-weight: bold; text-transform: uppercase; color: #153643; font-family: sans-serif;"> ${subtituloChamada04}</p>
							<p class="text" width="425px" height="425px" style="font-size: 14px; line-height: 21px; color: black; font-family: Roboto Slab, serif;"> ${textoChamada04}</p>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="header" style="padding-top: 32px;">
				<table align="left" width="425" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="425px">
							<#if imagemSegundoBanner??>  		
                                <div width="425" height="100%" alt="" border="0" style="color: #fff; background: url('${urlAPI}/newsletter/imagem/${imagemSegundoBanner.id}') no-repeat center center / cover; padding: 0px 0; min-height: 425px;" aling="left">
							</#if> 
            			</td>
 					</tr>
 				</table>
 				<table align="left" class="col425" cellpadding="0" cellspacing="0" style="background-color: #e3dbd2; border: 0; max-width: 425px; width: 100% !important; height: 425px;">
 					<tr>
 						<td height="425px">
 							<table height="425px" style="border: 0; padding: 50px;" width="100%" cellspacing="0" cellpadding="0">
 								<tr>
 									<td>
 										<p class="h2" style="font-size: 18px; font-weight: bold; text-transform: uppercase; color: black; font-family: sans-serif;"> ${tituloSegundoBanner}</p>
 											<p class="text" style="margin-top: 32; font-size: 14px; color: black; line-height: 21px; font-family: Roboto Slab, serif;"> ${textoSegundoBanner}</p>
 											<p align="center" style="margin-top: 32; text-decoration: none; display: block; -webkit-border-radius: 12px; color: black; text-transform: uppercase; padding: 16px; font-size: 12px; border: 2px solid black;" class="botao">
												<a href="${textoBotao02}">CONFIRA</a>
											</p>
 									</td>
 								</tr>
 							</table>
 						</td>
 					</tr>
 				</table>
 			</td>
 		</tr>
 		<tr>
 			<td>
 				<table align="left" width="850" cellpadding="0" cellspacing="0" class="footer text" style="background-color: #408559;  border: 0; font-size: 14px; line-height: 21px; font-family: Roboto Slab, serif; margin-top: 32px; color: white; padding: 30px;">
 					<tr>
 						<td> ${textoFinal}</td>
 					</tr>
 				</table>
 			</td>
 		</tr>
   </table>
   </div>
   <br>
   <div>
		<div alt style="border:0; background: url('${urlAPI}/arquivo/imagem/999979') no-repeat center center / cover; padding: 0px 0; height: 226px; width: 100%; background-size: 100%;"></div>
   </div> 
</body>
</html>