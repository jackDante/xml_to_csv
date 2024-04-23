<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:template match="*">
        <fo:root language="EN">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrail" page-height="297mm" page-width="210mm" margin-top="5mm" margin-bottom="5mm" margin-left="5mm" margin-right="5mm">
                    <fo:region-body margin-top="25mm" margin-bottom="20mm"/>
                    <fo:region-after region-name="xsl-region-after" extent="25mm" display-align="after" precedence="true"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrail">
            <fo:static-content flow-name="xsl-region-after">
	                <fo:table table-layout="fixed" width="100%" font-size="6pt" border-color="black" border-width="0.2mm" border-style="solid">
	                <fo:table-column column-width="proportional-column-width(20)"/>
	                <fo:table-column column-width="proportional-column-width(45)"/>
	                <fo:table-column column-width="proportional-column-width(20)"/>
	                <fo:table-body>
	                    <fo:table-row>
	                        <fo:table-cell text-align="left" display-align="center" padding-left="2mm">
	                            <fo:block>
	                            	Fattura elettronica generata
	                            </fo:block>
	                            <fo:block>
	                                Tel. 010 522.00.28
	                            </fo:block>
	                            <fo:block>
	                                Cell. 337/533.142 
	                            </fo:block>
	                        </fo:table-cell>
	                        <fo:table-cell text-align="center" display-align="center">
	                           <fo:block font-size="110%">
	                           		Empire Software srl
	                           </fo:block>
	                           
	                           <fo:block space-before="3mm"/>
	                       </fo:table-cell>
	                       <fo:table-cell text-align="right" display-align="center" padding-right="2mm">                 			                       		
	                           <fo:block display-align="before" space-before="6mm">Page <fo:page-number/> of <fo:page-number-citation ref-id="end-of-document"/>
	                           </fo:block>
	                       </fo:table-cell>
	                   </fo:table-row>
	               </fo:table-body>
	           </fo:table>
            </fo:static-content>
 <!--__________________________________________________________________________________________________________________________________________________________ -->
        <fo:flow flow-name="xsl-region-body" border-collapse="collapse" reference-orientation="0">
            
            
		<fo:table>
			<fo:table-column column-width="100%" />
            	<fo:table-header>
					  <fo:table-row>
					    <fo:table-cell text-align="right" display-align="center" padding-left="2mm">
					      <fo:block font-size="14pt" font-family="verdana" space-before="5mm" space-after="5mm">
				                DATI TRASMISSIONE
			               </fo:block> 
					    </fo:table-cell>
					  </fo:table-row>
				</fo:table-header>
				
				<xsl:variable name="Date" select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/Data" />
				<!-- format-date($Date,'[D01] [M01] [Y0001]') -->
				<fo:table-body>
			  		<fo:table-row>
			    		<fo:table-cell text-align="right" display-align="center" padding-left="2mm">
			    			<fo:block>
			           			nr: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/Numero" /> 
			           			in data: <xsl:value-of select ="concat(substring($Date, 9, 2), '/', substring($Date, 6, 2), '/', substring($Date, 1, 4))"/>
			       			</fo:block>		                      		
					        <fo:block>
					       		progressivo invio: <xsl:value-of select="FatturaElettronicaHeader/DatiTrasmissione/ProgressivoInvio" />
					        </fo:block>
					        <fo:block>
					        	nel formato: <xsl:value-of select="FatturaElettronicaHeader/DatiTrasmissione/FormatoTrasmissione" /> con codice id: <xsl:value-of select="FatturaElettronicaHeader/DatiTrasmissione/IdTrasmittente/IdCodice" />
					        </fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			
<!--________________CEDENTE PRESTATORE__CESSARIO COMMITTENTE________________________________________________________________________________________________________________________________________ -->
           	<fo:table table-layout="fixed" width="100%" font-size="10pt" space-before="15mm" space-after="5mm">
           			<fo:table-column column-width="proportional-column-width(50)"/>
	                <fo:table-column column-width="proportional-column-width(50)"/>
	                
					<fo:table-header>
					  <fo:table-row>
					    <fo:table-cell>
					      <fo:block font-weight="bold" font-family="verdana"  background-color="WhiteSmoke">CEDENTE PRESTATORE</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold" font-family="verdana"  background-color="WhiteSmoke">CESSARIO COMMITTENTE</fo:block>
					    </fo:table-cell>
					  </fo:table-row>
					</fo:table-header>
	                
	                <fo:table-body>
	                <fo:table-cell text-align="left" padding-left="3mm" padding-top="2mm">
	                	<fo:block>C.F.: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/DatiAnagrafici/CodiceFiscale" />
				         </fo:block>
				         
				         <fo:block>IdCodice: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/DatiAnagrafici/IdFiscaleIVA/IdCodice" />
				         (<xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/DatiAnagrafici/IdFiscaleIVA/IdPaese" />)					      
				         </fo:block>				         
				         
				         <fo:block>Denominazione: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/DatiAnagrafici/Anagrafica/Denominazione" />
				         </fo:block>
				         
				         <fo:block>Indirizzo: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Sede/Indirizzo" />
				         (<xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Sede/CAP" />, <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Sede/Nazione" />)					         
				         </fo:block>
				         
				         <fo:block>Comune: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Sede/Comune" />
				         (<xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Sede/Provincia" />)					         
				         </fo:block>
				             
			             <fo:block>Telefono: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Contatti/Telefono" />
			             </fo:block>
			             <fo:block>Email: <xsl:value-of select="FatturaElettronicaHeader/CedentePrestatore/Contatti/Email" />
				         </fo:block>
			        </fo:table-cell>
			        <fo:table-cell text-align="left" padding-left="3mm" padding-top="2mm">
					        <fo:block>C.F.: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/DatiAnagrafici/CodiceFiscale" />
					        </fo:block>   
				           <fo:block>Denominazione: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/DatiAnagrafici/Anagrafica/Denominazione" />
				           </fo:block>
				           <fo:block>Indirizzo: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/Sede/Indirizzo" />
				           (<xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/Sede/CAP" />, <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/Sede/Nazione" />)
				           </fo:block>
				           <fo:block>Comune: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/Sede/Comune" /> (<xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/Sede/Provincia" />)
				           </fo:block>
				           <fo:block>Nome: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/RappresentanteFiscale/Nome" />
				           </fo:block>
				           <fo:block>Cognome: <xsl:value-of select="FatturaElettronicaHeader/CessionarioCommittente/RappresentanteFiscale/Cognome" />
				            </fo:block>
	                	</fo:table-cell>
	                </fo:table-body>
	            </fo:table>
	            
	            
<!--______________PRODOTTI E SERVIZI____________________________________________________________________________________________________________________________________________ -->	              
	            
	            <fo:block text-align="center" font-size="14pt" font-family="verdana" space-before="5mm" space-after="5mm">
	                <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt" />
	                PRODOTTI E SERVIZI
	            </fo:block> 
	                   
	            <fo:table table-layout="fixed" width="100%" font-size="10pt">
           			<fo:table-column column-width="proportional-column-width(3)"/>
	                <fo:table-column column-width="proportional-column-width(30)"/>
	                <fo:table-column column-width="proportional-column-width(7)"/>
	                <fo:table-column column-width="proportional-column-width(10)"/>
	                <fo:table-column column-width="proportional-column-width(8)"/>
	                <fo:table-column column-width="proportional-column-width(5)"/>	                
		            <fo:table-column column-width="proportional-column-width(5)"/>                       
	                <fo:table-header>
					  <fo:table-row>
					    <fo:table-cell>
					      <fo:block font-weight="bold">NR</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold" content-width="scale-to-fit">Descrizione</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Quantita</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Prezzo Unitario</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Prezzo Tot.</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">IVA</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Ritenuta</fo:block>
					    </fo:table-cell>
					  </fo:table-row>
					</fo:table-header>
					
					<fo:table-body>
						<fo:table-row  background-color="WhiteSmoke">
						    <fo:table-cell>
						      	<fo:block>	
						         <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/NumeroLinea" />
						   		</fo:block>
						   	</fo:table-cell>
							<fo:table-cell>
								<fo:block>
							     <xsl:value-of select="normalize-space(translate(translate(FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/Descrizione, '&#10;', ' '), ',', ' '))" />	
								</fo:block>
							</fo:table-cell>						
							<fo:table-cell>
								<fo:block>
							          <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/Quantita" />
							    </fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
							          <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/PrezzoUnitario" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
							          <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/PrezzoTotale" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
							          <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/AliquotaIVA" />
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
							          <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DettaglioLinee/Ritenuta" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
                </fo:table-body>
            </fo:table>
            
            
<!--_____________DATI PAGAMENTO_____________________________________________________________________________________________________________________________________________ -->	            
                	<fo:block text-align="center" font-size="14pt" font-family="verdana" space-before="20mm" space-after="5mm">
		                <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1pt" />
		                DATI PAGAMENTO
	            	</fo:block>
                	
                	<fo:table table-layout="fixed" width="100%" font-size="10pt" space-before="5mm" space-after="5mm">
           			<fo:table-column column-width="proportional-column-width(20)"/>
	                <fo:table-column column-width="proportional-column-width(20)"/>
	                <fo:table-column column-width="proportional-column-width(30)"/>
	                <fo:table-column column-width="proportional-column-width(30)"/>                                    
	                <fo:table-header>
					  <fo:table-row>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Condizioni</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Metodo</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">Istituto Finanziario</fo:block>
					    </fo:table-cell>
					    <fo:table-cell>
					      <fo:block font-weight="bold">IBAN</fo:block>
					    </fo:table-cell>				    
					  </fo:table-row>
					</fo:table-header>
					
					<fo:table-body>
						<fo:table-row background-color="WhiteSmoke">
						    <fo:table-cell>
						      	<fo:block>	
						        	<xsl:value-of select="FatturaElettronicaBody/DatiPagamento/CondizioniPagamento" />
						        </fo:block>
						    </fo:table-cell>
						    <fo:table-cell>
						        <fo:block>
			          				<xsl:value-of select="FatturaElettronicaBody/DatiPagamento/DettaglioPagamento/ModalitaPagamento" />
			          			</fo:block>
			          		</fo:table-cell>
						    <fo:table-cell>
			        			<fo:block>
					           		<xsl:value-of select="FatturaElettronicaBody/DatiPagamento/DettaglioPagamento/IstitutoFinanziario" />
						   		</fo:block>
						   	</fo:table-cell>
						    <fo:table-cell>
						        <fo:block>
			           				<xsl:value-of select="FatturaElettronicaBody/DatiPagamento/DettaglioPagamento/IBAN" />
			           			</fo:block>
			           		</fo:table-cell>			
						</fo:table-row>
												
                </fo:table-body>
            </fo:table>
                	
                	
                	
                	
            <fo:table table-layout="fixed" width="100%" font-size="10pt" space-before="5mm" space-after="5mm" border-color="red">
		        <fo:table-column column-width="proportional-column-width(30)"/>
			    <fo:table-column column-width="proportional-column-width(70)"/>
                	
               	<fo:table-body>
	                <fo:table-cell text-align="left" padding-left="3mm" padding-top="2mm">
	                	<fo:block>
	                		Divisa: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/Divisa" />		        
				        </fo:block>
				        <fo:block>
							Tipo Ritenuta: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/DatiRitenuta/TipoRitenuta" />
				        </fo:block>
				        <fo:block>
	                		Importo Ritenuta: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/DatiRitenuta/ImportoRitenuta" />		        
				        </fo:block>
				        <fo:block>
	                		Aliquota Ritenuta: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/DatiRitenuta/AliquotaRitenuta" />		        
				        </fo:block>
				        <fo:block>
	                		Causale Pagamento: <xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/DatiRitenuta/CausalePagamento" />		        
				        </fo:block>
				        <fo:block>
	                		Esigibilità IVA: <xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/EsigibilitaIVA" />		        
				        </fo:block>
				     </fo:table-cell>
					     
					     
					 <fo:table-cell text-align="left" padding-left="3mm" padding-top="40mm">
					     
						 <fo:table table-layout="fixed" width="100%" font-size="10pt" space-before="5mm" space-after="5mm">
			           			<fo:table-column column-width="proportional-column-width(30)"/>
				                <fo:table-column column-width="proportional-column-width(30)"/>
				                <fo:table-column column-width="proportional-column-width(30)"/>
	                	
		                		<fo:table-body>
							                <fo:table-row>
												<fo:table-cell text-align="right" padding-left="3mm" padding-top="2mm">
											    	<fo:block font-weight="bold">Imponibile Importo</fo:block>					           	
											    </fo:table-cell>
												<fo:table-cell text-align="right" padding-left="3mm" padding-top="2mm">
											    	<fo:block font-weight="bold">Imposta</fo:block>					           	
											    </fo:table-cell>
											    <fo:table-cell text-align="right" padding-left="3mm" padding-top="2mm">
											    	<fo:block font-weight="bold">Importo Totale</fo:block>
											    </fo:table-cell>
											 </fo:table-row>
											 <fo:table-row background-color="WhiteSmoke">
											 	<fo:table-cell text-align="right" padding-left="3mm" padding-top="1mm">
											    	<fo:block><xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/ImponibileImporto" /></fo:block>					           	
											    </fo:table-cell>					
												<fo:table-cell text-align="right" padding-left="3mm" padding-top="1mm">
											    	<fo:block><xsl:value-of select="FatturaElettronicaBody/DatiBeniServizi/DatiRiepilogo/Imposta" /></fo:block>					           	
											    </fo:table-cell>
											    <fo:table-cell text-align="right" padding-left="3mm" padding-top="1mm">
											    	<fo:block><xsl:value-of select="FatturaElettronicaBody/DatiGenerali/DatiGeneraliDocumento/ImportoTotaleDocumento" /></fo:block>
											    </fo:table-cell>
											 </fo:table-row>
											 
											 <fo:table-row>												
											    <fo:table-cell>
											    	<fo:block> </fo:block>					           	
											    </fo:table-cell>
												<fo:table-cell text-align="right" padding-left="3mm" padding-top="2mm">
											    	<fo:block font-weight="bold">Data Scadenza</fo:block>					           	
											    </fo:table-cell>
											    <fo:table-cell text-align="right" padding-left="3mm" padding-top="2mm">
											    	<fo:block font-weight="bold">Importo</fo:block>
											    </fo:table-cell>
										 </fo:table-row>
										 <fo:table-row>										 	
										    <fo:table-cell>
										    	<fo:block> </fo:block>					           	
										    </fo:table-cell>					
											<fo:table-cell text-align="right" padding-left="3mm" padding-top="1mm"  background-color="WhiteSmoke">
												<xsl:variable name="Date2" select="FatturaElettronicaBody/DatiPagamento/DettaglioPagamento/DataScadenzaPagamento" />
										    	<fo:block><xsl:value-of select ="concat(substring($Date2, 9, 2), '/', substring($Date2, 6, 2), '/', substring($Date2, 1, 4))"/>
										    	</fo:block>					           	
										    </fo:table-cell>
										    <fo:table-cell text-align="right" padding-left="3mm" padding-top="1mm"  background-color="WhiteSmoke">
										    	<fo:block><xsl:value-of select="FatturaElettronicaBody/DatiPagamento/DettaglioPagamento/ImportoPagamento" /></fo:block>
										    </fo:table-cell>
										 </fo:table-row>
						     	</fo:table-body>
						     </fo:table>	
					     </fo:table-cell>			     
				     </fo:table-body>
	                </fo:table>
                               
                	<fo:block id="end-of-document">
                  	</fo:block>
            	</fo:flow>
            	
            	
            
        	</fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>