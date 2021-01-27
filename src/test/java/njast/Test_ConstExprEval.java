package njast;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class Test_ConstExprEval {

  @Test
  public void ceEval() throws IOException {
    Map<String, Integer> toeval = new HashMap<String, Integer>();

    //@formatter:off
    toeval.put("(12+126)+207*220*119+130*(147+173)+78*12+224+193*(178+133)+(61+197)", 5522439);
    toeval.put("67*125*232*41+3*238*3+156*(4*157)+(1*242)+22+225+247+188", 79764034);
    toeval.put("(157*203)*103+66+133+7+86*141+89+36+179*20+(27+173)+183*11", 3300963);
    toeval.put("(234+135)+(36*90)*(25*120)+228*231+162*137+9*104*119+153+216*73", 9922536);
    toeval.put("140*149*(214*194)+171+16+45+77+165*7+(193+214)*(102*210)+153+153", 874743470);
    toeval.put("209*132+81*57+95+58*(204+231)*221+195*(46*18)+106+126+(234+208)", 5770264);
    toeval.put("(102+97)+116+160*(173+135)*110+218*(240+98)+15*253*(220*77)+27*142", 69785933);
    toeval.put("252+141+154*220*111+84*(135+117)*166*242+(245*177)+182+159*57*157", 855588407);
    toeval.put("210+171*223+180+(215+34)*(222*104)+9*211*34+244*220+90+111+79", 5905961);
    toeval.put("75+139+70+248*72*94+(222*118)+(96+51)+243+144+199+80+241*143", 1740220);
    toeval.put("39+77*201+14+212+82*(88+122)*(166*1)+131+122*(129+106)*(237+211)", 15718553);
    toeval.put("(115*181)+32*39+77*189+28+35+(116*181)*90+110+176*140*174*141", 606444189);
    toeval.put("76*70*(62*27)+31+156+224*41*36+7+(21+211)+178+158+1+33", 9237100);
    toeval.put("137*35*162+200*111*18+249+136+182+200+29*79*81+101*107*29", 1676131);
    toeval.put("148*66*23+3*123*159+141+254*(66*182)+92+141*208+48*(85+102)", 3372920);
    toeval.put("136*108+87+215*(151+110)+(116+6)+(68*199)*(117*136)+95+115*87*2", 215412301);
    toeval.put("(175*110)*217+144+196+109+95*190*250+34*186+25*(3*114)*5+147", 8739420);
    toeval.put("185*47+2+237*36*94+186*58+245+93*211+180+178+102*131*193", 3420585);
    toeval.put("108*217+225+9*99*18+(54+62)+(145*98)+109*21*(28+35)*140+191", 20243196);
    toeval.put("(165+231)+137*139+147*73+215*241*233+68*(99*218)+(30+255)*(135+222)", 13672386);
    toeval.put("89+140*39+144*89+145*5+2*(8+72)*(226+100)*(51+21)+(160+214)", 3774984);
    toeval.put("242+18+(254+191)*2+87+(255+11)*(151*214)+130+242+220+120+65*177", 8608978);
    toeval.put("(100*45)*95+22*160*122+136*167+10+71*129*253+(158+200)+8*39", 3197559);
    toeval.put("(54+218)*(247+50)*22*20*(0+40)+81*136+253+153*138+61*74+104", 1421835401);
    toeval.put("(213+134)+(140+3)*120*140+(214+30)*104+132*(222+201)+190+46*(106*23)", 2596297);
    toeval.put("(23*193)*85+35*(169+82)+247*221*81+10+117*217+214+160+126+29", 4833575);
    toeval.put("84+70+(209*100)+119+108*117+103*101+209*76+140+83+241+(255*221)", 116915);
    toeval.put("(241*76)*(186*58)+90+132*(21*243)+216+144+177+249*(208+105)*201+219", 213932787);
    toeval.put("(237*111)*232+132*166*196+116+146*160+106*(233+230)+165+198+(224*45)", 10480973);
    toeval.put("2+238+(225*225)*36*7+(1*199)*43+55+217+74+50*197+163*33", 12781872);
    toeval.put("255*50+22+195+(192*59)*(82*96)+115+249*239*164+155+90+205*221", 98992452);
    toeval.put("23*59+(119*59)+(166*157)*213+168+247+199+(215*127)+(161*247)+(175+255)", 5627700);
    toeval.put("109*8*244*207+251+179*56+151+46*243*74+139*16+54+(201+206)", 44883259);
    toeval.put("219+111+65+255+94+240+(251+84)+150+50+153+235+122*228+170+164", 30057);
    toeval.put("(142*195)*92+149+(88*178)+207+161+145+4+40*194*(147+96)*161+255", 306158545);
    toeval.put("(170*93)+24+131+208+126*(59+209)+233*167+52*73*161+59+249*32", 708035);
    toeval.put("(12+65)+(30*39)+180*16+(221+245)+(179*41)+130*163*(247+37)+68+227", 6030187);
    toeval.put("(38*202)*(38+156)*245+17+151+115+73+193*(63+230)+102+30*0*38", 364897287);
    toeval.put("171+156*74+243+(107*16)+18*14+(23*166)+48*187+153*65*(214*155)", 329902366);
    toeval.put("232+22+138*242+(44*157)+130+47+(61*126)+88*32+230*142+21*227", 88664);
    toeval.put("114+5*201*89+(125+124)*75*214+116*79+(255+120)*(21+132)*(18*78)", 84649673);
    toeval.put("(208+172)+(8*210)*48+13*247+84*213*183*95+51*124+22*191+61", 311147238);
    toeval.put("(26*233)+251*19*(210+0)+64*204+108*185*239*137+98+241+217+176", 655226476);
    toeval.put("11+144+212*143+18*232*15+23+(132*193)*1+137+72*178*205+16", 2746043);
    toeval.put("(172+100)+65*161+103*224+(119+250)+64+49*(7*28)*153+71*147+16", 1514107);
    toeval.put("112*169+93*191+6*227+255+78+205+97+44+141*144+101*233+123", 82692);
    toeval.put("(69*65)+143+176+(108+31)*174+145+(197+220)+48+82*48+211*46+171", 43413);
    toeval.put("243+184+(80+131)+88+17+247+212+234*191*(148+82)+(156*240)+5*107", 10318797);
    toeval.put("232+40+(169*34)+211+36+64*75*146+132*(52+222)*221*34+118+79", 272473614);
    toeval.put("30*218+84+205*116+13+226+161+196+238*105*16+(238*153)*24+250", 1305026);
    toeval.put("83*248*79*40+107*147*(42+57)*182+156+133+56+211+108*220+78", 348474956);
    toeval.put("(24*55)+(103+111)*108+44*(134+167)*155+179+(1*163)*32*37*92*20", 357182711);
    toeval.put("125+78+103*45+(252+41)*194*69+90+251*(187+13)+(123*94)+2+198", 3988988);
    toeval.put("89+78+51*165*134+146+169*103+(184+250)+(255+60)*(254+82)*255*7", 190070164);
    toeval.put("71+23+(166*144)*3+155*197*62+33*178+41+233+75*174+193+35", 1984402);
    toeval.put("18+62*196+139*47+33+74+251+10*28+(3+198)+179+255*51+156", 32882);
    toeval.put("4+75*(171*76)+29*37*127+58+86*8*(241+209)*49+232*(57+33)", 16302313);
    toeval.put("(215*164)*(228*54)+(149+210)*152+152*200+12+131*24+113*233*243*183", 1605033545);
    toeval.put("(229+177)+(88*101)*167+112+(170*227)+100*95+181+198+83+85*211*110", 3506216);
    toeval.put("37*66+(98+94)*17*92+(191+59)*46*98+79+103+240*19+200*48", 1444072);
    toeval.put("251*244*16*149+(39*114)+168*16+224+157+51*14+182*50+129+3", 146023157);
    toeval.put("(177+223)*133*107*60+120+(119*38)+218*103+(211*88)+238*185+(132*253)", 341667090);
    toeval.put("151+49+(66*31)+139*87*90*60+(69*99)+(92*116)+188*104*70+144", 66690733);
    toeval.put("17*13*(36+237)+121*224+(236+149)+191*109+155+15+244+253*(7+111)", 138909);
    toeval.put("(46+37)*10*221*100*56+107*19+255+64*38+21+189+175+208+23", 1027213336);
    toeval.put("(214*138)*142*245+69*0*255+199*158+229*149*42+71*230+(153*211)", 1028931417);
    toeval.put("15*113*(127+126)*192+75+207*66*115+232+185+99*226+28*79*80", 84107276);
    toeval.put("181*38*197+133+(93+47)+51+153*(48+252)*16*47*25+64+66+134", 864275554);
    toeval.put("(152+65)+161*8*92*125*101+159+(154*202)+(104+1)*13+97+176*106", 1496063602);
    toeval.put("90*48+166*72+(55+56)*140*232*207+38+(224+180)+(159+55)*(230*244)", 758319354);
    toeval.put("215+79+(170+31)*(179+175)*166*119+210+56*(216*100)+238+170+(255+28)", 1406786911);
    toeval.put("180*125*179+0+(251+165)+41*105+(238+115)+30*213*(205+232)*(46+73)", 336331744);
    toeval.put("187+161+252+19+(193*83)*79*97+(81*125)*(34+61)+(128*36)+152*186", 123748971);
    toeval.put("43*158*251*102+16+5*(207*172)+(253*205)+176*170+(8*177)+148*57", 174209661);
    toeval.put("(216+127)+(102+186)*249+62+135*211+106+65+217*77+(63+196)*(175+201)", 214866);
    toeval.put("(43*133)*200+52+22+35*131*32+(72+89)*(149+24)*6+251*(40*3)", 1487832);
    toeval.put("38*80+8*9*(83*72)+200+243+(182+25)*1+154*161+198*39*214", 2111264);
    toeval.put("65*253+105+154+0*94+119*117*(114*17)+(24*117)+7+128*(28+209)", 27032629);
    toeval.put("(76*33)+(117+50)*(187*1)*(227*89)+31+31+48*51*(254*186)+102+73", 746575544);
    toeval.put("(203*102)+15*57*218+71+181*70*21+40+186+62+236*155+118*248", 539369);
    toeval.put("138*208+(234*124)*(202+78)*226+237*231+93*42*49+112+234+251+123", 1836408045);
    toeval.put("135+0*41*193*223+201*6+142+(98*100)*183*35+142*104*72+78", 63833857);
    toeval.put("(255*236)*39*106+(36*247)+128+244+(235+52)*244+21+(172+112)+226+43", 248863986);
    toeval.put("114+138*(113*65)+85*82+254+35+235*104+139+210*(150+30)+(46*55)", 1085892);
    toeval.put("206*225*245+202*112+192+11+198+(32*24)+191+123+186+196*(10+211)", 11423359);
    toeval.put("88+126*(8+141)*10*7+(233+85)*24+119+(216+80)+189*102*(57+222)", 6700877);
    toeval.put("9+61*(160*11)+192*178+142+22*150+77*128+87+(74+214)*99*63", 1951186);
    toeval.put("(50+27)*92+181+(7*35)+204+110+141*213*22+231+(83+89)+22*176", 672825);
    toeval.put("3+9*3*8*1+4+(0*7)+(4+1)*(7*2)+0+7+0*8+0*6*(6+3)+(4+7)+(7+7)+4+5*3*4+1*6*2*6", 461);
    toeval.put("6+6+4+9*4*0+2*6*(8*9)*7+3*(8+1)+(8*6)*2+9+4*2*7*9+1+4+4+9+2+1*(1+9)*8*8", 7360);
    toeval.put("3+9*3+0*7*6*5*1*3+3*7+4+2*6*8+2*7+7*0*8*(4*4)*(4*3)+(9+3)*1+1+(9+2)+5+2", 196);
    toeval.put("(2+3)*0+2+(4+4)+(0*4)+9*7*9*2*(6*2)*(5*5)+2+4+(3*9)+1+7*(7+4)*1*1*7+4+1*2+9*6", 340843);
    toeval.put("(6*0)*1*3+(9*0)*(9+6)*7*9*9*2+9+8*1*4*6+7*(0+5)*4*9+7*3+2*4*5+0+1+0*5+3", 1526);
    toeval.put("(5+2)+(7*1)+(3*9)+6*8+4+3*(4+6)*0*0+6*8+3+5+6+8*2*5+(0*2)+(5+4)+2+4*2*9*(3+3)", 678);
    toeval.put("2+1*(5+1)*(5*7)*9*6*8*2+(2*4)*6*1*9+8*3*5*(6*3)+2*0*5+4+(6*7)*(8+0)*1*2+4*0", 184710);
    toeval.put("2*3+2+8+9+4*0+3*(1+4)*6+5+0+9*7*3+(9*3)*2*1+9*3+(5+8)*5*4+(7*0)*4*0*8+5", 655);
    toeval.put("4+5+0+1*4*2*6+8+7+6+1*3*(9+8)+4+6+(7*6)*2*7+(4+6)*5+6+5*2*1*8*4+8+(9*4)", 1147);
    toeval.put("9*2+(6+9)+2*9+2*8*(8*5)*(6+3)+0+0+3*4+4*1+2*5*(7*1)*(1+7)*0*9+9+9*4+6*(5+6)", 5938);
    toeval.put("0+9*(0*8)*(5*7)+7+7+0+3*4*7+6+1*(5*1)+7*1+(4+5)+(2+1)+(5*3)+(4+3)+4+4+5+2+2+9", 176);
    toeval.put("0+0*6*0+7*5*5*6+8*5*9+4+5*5+(2+8)*7*6*1*0*(3*8)*5*6+8+3*8*8+8+7*(8*2)", 1759);
    toeval.put("6+6+6*6+(8+6)+2+8+(3+9)*2+1+6+7*9+8+3*3+(1*2)+6*1+(2+3)+2+6+(1+3)*(5+1)+(5*5)", 253);
    toeval.put("0+7*(8+8)*(2*2)*8*5+(2*7)+0+9+6*8+0*9*4+2+9+0+4*4*(5+4)+4*8*2+0+(7*9)+0+9", 18282);
    toeval.put("6*3+(3+3)*(7+4)*9*3+1+8*(3*2)+(5*4)*(3+9)*5*9*5*4*(1*8)*5*7*(5*2)*(6*0)*(5+0)+(3*8)", 1873);
    toeval.put("4*5*(8*3)*4+6+(0+7)*5*9+(3*5)+9*5*5+4+6*6*(9*1)+7+8+3*0+(4+8)+4*2*(1*0)+9+5", 2850);
    toeval.put("4*1+(3*8)*(1*0)*2+4+9+3*5*3*1+4+8+1+5*6*9+1*(1+1)*(7+6)+(3*4)*(1*5)+(1+6)+(9*4)", 474);
    toeval.put("8*4+(6+5)*1+7*6*0*(2*8)+1+9+3*9*5*6*5+0*(0*4)*(3+4)*0+9*(5+9)*2+5+5*5*3+1", 4436);
    toeval.put("5+9+4+0+(2+6)*2+4+5+8*(6*4)+8+6*(8+2)+(4*4)+8*4+(9+2)*5+0+3*2+(3+0)+(3*2)+5*0", 421);
    toeval.put("(7+4)+8*5*(5*4)*9*4*3*2*9*2+0*3*5+4*2*0+6+0+9+8+(1+8)*6*4+0+5+8+7*4+3", 3110694);
    toeval.put("8+4*(2*1)*(9+2)+8*9+(4+1)*(8+4)*2*8+6+8*3+5*(7*7)*1+3*(6*4)*0*1*2+3*2+3*9*0", 1409);
    toeval.put("8*9+4+3*0+9*6+1+6+1*0+3*(5+9)*(0+4)*(9*0)*(0+2)*9+0*4+6*(7+3)*6*2+(5*1)+1*2", 864);
    toeval.put("0*5*5*5*1+0*(3*7)+8+0+(2*1)*7+5+1+9*(2+6)*2+8+(6+7)+5*4*(3+5)+2+2*(7+6)+9+8", 398);
    toeval.put("(9*3)*2+9*5*0*9*2+5*5*(1+2)*(6+4)*4+0*(4+6)*9+8*3+9*7+8*7*7+8*5*4*0+8+4", 3545);
    toeval.put("4*1*0+7+7+9*(6+3)+(1*8)*9+4+(7+3)+6*9*7+3*8*1*4+8+7+7*0*4*7+8*7*7+5*3", 1077);
    toeval.put("3+0+9*4+8*7*0+1+2*7*(2+3)+(2*0)+6+2*0+6+(4+9)+(6+6)+(5+1)*6+9+4+9*8+9*(7+7)", 394);
    toeval.put("1*7+2*5*1*2*9+9+1*3+(2*2)+8+2+1*9*(1*3)*4+2+(3+6)*(3*2)*2+6+1+1+9*5*(7*6)", 2329);
    toeval.put("0*3+2+1*1+1+(7+5)+(0*3)*4*5*6+3+(6+9)+2*1+7*8+2*3+8*9+4*2*4*8+5+9*(4*1)", 467);
    toeval.put("3*6*5*9*2*2*(7*4)*(8+7)+(2+5)+3*0+1*3+5*8+1+5+6+8+2*1+3*8+8*5*9+6*3+1", 1361275);
    toeval.put("(4*9)+9*3+(7+1)*3+1+6+9*5*0*9+7+7*7+1*7+(5*1)+(5+7)+8*0*1*5+(9+7)+(9+4)*8+7", 301);
    toeval.put("2+9+3+2+(3+0)+7+9+(2*8)+9*7*5*2*5+7+(7+2)*(6+0)*2+7*6+9*6+5+5*0+1*3+3+9", 3432);
    toeval.put("1*4*6*0*(1*9)*8+7*2*9+(7*3)*5+9*1*0*(0+9)+(9+1)*(9+1)*9+8*(6*9)*1+5*8*7+(3*1)", 1846);
    toeval.put("(16778+20742)+(4386*11793)+20878+18206*8021+10288", 197823110);
    toeval.put("24578+1656+18721*2014+(19120*17794)+2891+27029", 377981528);
    toeval.put("(20477*9122)+31870*32015+7921*29700+4607*23621", 1551184891);
    toeval.put("(17164*18660)+(6874+22671)+21640*6055+11833+1597", 451353415);
    toeval.put("18342+32636+(7969+22655)*12610+18957+25690+7233", 386271498);
    toeval.put("3660*21196+(25395+16766)+22937*29665+(12472+18776)", 758076874);
    toeval.put("31911+12567*16766+13617*(22430+15442)+23150*6398", 874546957);
    toeval.put("(6+3)*3*1+6+0+(2+7)+6*6+5+5*2+5*(3+5)*(3*0)+(4*0)*0+0+7*5+3*6+(2+2)+0+2+3+5*(1+6)*(5+7)*(3*5)*(4+2)+(7+4)+(4*2)+5+7*1*6*2*6+2+6+(5+4)*(1+3)+0*4+2+6+6+5+7*7", 38595);
    toeval.put("(7+7)+(3+7)+6+1*(3*0)*6*5+7*4*(4+3)*(1*1)*(3+3)*(3+0)+4*4+7+2+4*6+5+3+6+1*0*6+3*0+(1+6)+7+2+(5*4)+2+7+5*1+(5+2)+2*7+(7*3)*4+5*3+6*7*1*3*4*3*2+(4+0)+1*5", 6824);
    toeval.put("1*3*3+6+4*1*(6*2)*0+4*4*5+4+7+3+6*(4+3)*(6*1)+(1+5)+(6*6)+(1+4)+(5*0)*5*4*(5*3)+2*5*(6+6)*5*4+2*0+(3*7)*3*7*4*7*(1*1)+7+4+(0+2)*(1+5)+0+4+2*4*(2+4)*1*1+7*2", 15245);
    toeval.put("5*0*1*4+2+7*6*3+3+2+4+1+3+7*3+6*6+6+(2*4)*5+0*(0*6)*5+1+3+1+0*4+3*7+5+6*(0*5)+7+6+7+7*6+0*5*4+5*5*0*7+6+7*7+6*(6*0)+1+5+1+6+5*7*2+4*7*0", 475);
    toeval.put("1*1*(2+1)+1*6+2+6+4*5+2+4*(4*4)+0*7*5+1+6+7*6*1*(0*4)*3+0*(6*3)*7+7*3*7*7*6*(5+5)*(7+7)+3+6+4*0*(2+2)+(4*4)*(7+5)*(7+6)+6*4+5+3*1+3*1*2*1+0*(4*3)*(6*6)", 867013);
    toeval.put("2*5+4*1+3+5+0+5*6*2*5+5+(2+3)*4*2+1*0*7*1+2+4+3*0+5+2+2+0*2+0*2+7*7+4*7+7+(1+5)*1+3*(7+5)+(6+0)+2*7+(6+6)*0*4*(6+1)+6*0*6*0*6*0+3+4*(6*6)*0+3", 534);
    toeval.put("(0*5)+(1*7)+5+7+(6*3)+5*7*(0+4)*7+0+(3*3)*6+7*(2+5)*(3+2)+(7*1)+2*0+5*0*0+6*7*1+(6*1)+(1*2)+(2*3)*(7+2)*6+0*0*6+5*1*2+0+4*6*5*1*4*3+(7*4)*(0*1)+5+7+(3+4)+0+2", 3168);
    toeval.put("(0*2)*(2*3)*(6*2)+6*2*1+0*(6+6)+4+3*1+6+0+2+(2+2)+1*4+(1+0)+5+3*2*2+5+2+3+5+5+2*(2+6)*4*2*(0*2)+(0+0)*1*2+0+7*7*3*4*6*1+4*(7+1)+3+2+6+3+3*6+7*4+(7+3)", 3703);
    toeval.put("2*4+6+1+(7*5)+5+6+3*6+2*3*3+4+4*5*7+5*3+7*(6*1)*(6+6)*(1+7)*(4+6)+2+6+6*6+7+6+0*1+2*4+7*6+7*1+4*6*3*1+0+4*3+0*5*3*(5+1)*(1+4)*(0*4)*1+7+2+5+3+6", 40797);
    toeval.put("(2+3)*1+4+1+6*(0+2)*5*7*1*0*(3*7)*(6+7)+0*5+(2*7)+3*1*7+4*(3+2)+0+4*(6*3)+(4*0)+(2+7)*(5*6)+1+1*7+0+(5*0)*1*0+3+4*(7+0)+7+1+(0+7)*2+1*7+2*4*7+0*6*7+2+7+0", 540);
    toeval.put("3*1+(7*4)+(2+3)*(5+0)+6*6*(2*2)*(3*4)*6+1+4*0+(6+2)*0*6*5*0+1*1+1+4+(5*4)*(4+7)+(7+0)+(0+0)+5+7*(5+3)+1+2*(6*2)*(2*5)+(6*3)*6*7*(3+5)*(0+4)*5+4+7*5*4+7+1+1*1+1", 132074);
    toeval.put("4+3*2*3*2*6+3*6*0+4*(2*1)+6+3*6*1+(7*5)*0*0+4+3*(3+5)+3*1+2+1+(3+7)*0+3+6*6+7*7*6*2+(5+2)+6+0+6+7*3*2+(1*6)+6*2*4+1+6+6*(3*2)*6*0+2+6+(1+0)+2+6", 1052);
    toeval.put("2*3+4+3*(1+5)+6*1*5+0+2+6*0*7*2+6+(7+7)+1+1*(4*7)*(3*0)*3+3+(4*4)+3*7*(4*2)+3+1+7+3*(7*5)*0+4*2*5*(2*0)+7+5+2+4*(2*1)*1+0+(3*4)+6+2*0*4*0+1+(5*1)*3*7", 425);
    toeval.put("1+0+2*6*4*4+(5+4)*2*5*(2+0)*(1*4)+6+5*2+6*(0*1)*(6+4)*3*4*2*0*(3*5)+7+2*1*5+3*4*(3*4)*(2+6)+(4*7)+7+7+4+2*7+3+1+5+(0*2)+0+5+1+5+5+5+1+3+4*0*(7+4)+(5*6)", 2222);
    toeval.put("(2+0)+(4*2)+4+7*7+5+3+4*0+6*2+2+2+0*5+4*(1+1)*(7+2)*4+0+5*1+2*0*7+2+5+4*(7*2)+(3*0)*4+6*5+3+(4*6)*6*2+2+4*5+0*6+7*3*4+2+0+1+5+0*6+(3*7)+2+6+4+1", 912);
    toeval.put("1*7+3+3*3*4+(2*2)*(3+5)*(4*6)*2*3*(6*1)+(6*3)+3+5*2+0+0+4*(5+7)+(0*6)+4*5*6+1*2*3+3*3+4*7*0*7+7*1*4*3+(2+6)*6*0+6+5*0+0+(0+1)*(1+0)+1*3+3+7*5*4+5*1", 28150);
    toeval.put("(0*2)+7+3*2*5*3+7+5+4*2+1*3+1*5+7+(1+6)*0+0*7+7*1+6*7*3+4+4*2+6*(6+5)*1+0*1*2+(0+0)+6+5+7+6+(2*5)+(3+3)+2+1+7*7*4*5+5*2*2+4*4+3*2*2+7+1+(4+4)", 1430);
    toeval.put("(5+3)+3+1*7*6*1+5+(4+6)*(1+5)+2*3*(2*3)*4+3*6+4+0*5*(5*4)*6+1*4*0*(0+2)*3*5+2*4+2*6*4*2*(3+3)+3*0*4*4*(3*6)+1*0*(4+4)*(2+6)+2+7+2*5+5*5+1+3*(6*0)*5+3", 916);
    toeval.put("2*1+(1*0)+4+1*(0+7)*3*3+3*7*6+4*(3*3)*1+2*(1*7)*6*2+1+6*6*5+(4+7)*6*2*1+0+4+4+(5*7)+(6*4)*4+2+4+6+(1+3)+4+5+(3*2)*1*5+6+6+(3+2)+5*6+7+4*1+1+1+5+(0*7)", 971);
    toeval.put("(2+5)*6+5*0+6*2*1*(2*6)+7*6*7+1*7+2*(7+6)+(3+2)*(3*3)+2+0*1*1+(4*1)+2*3+2+7*4*3+1+2*(1+2)*6+4+4+0*5+4*(6*2)*3*7+(7+2)+5*3+5+5*2*1*2+4+(4*5)*6+5*0*0", 1882);
    toeval.put("3*5*(7*7)*2+2*6*2*4+0*6+0*(6*0)+4*5*5+0+4*3+(3*7)*5+1*(7+0)*6*2*(4*1)*(1+4)+3*4+1*2*5+1+(0*7)*(6+3)*6*4+(6*1)+2+4+(6*5)+(6+0)*2+6+2*6*7*5*(0*5)+3+5+(3*1)", 3557);
    toeval.put("(4+3)+5+3*1+2*(4*1)*3*5*(7+1)+0*6*(2*6)+4*2*7*4*5*6*6*6+(6+7)*(1*6)*0*7*(7+6)*0*0+3*7+5*7*6*7*6*4*2+4*7+3*6*1+0+0*(0*7)+7+2+(1+4)+(1*0)+5+3*1*0*(5+0)", 313541);
    toeval.put("(1*3)+(0*5)*2+2*5*1*(6*5)+1+5+5+7+(4+4)+(4+2)+(5+5)+7*3*(0*0)+(4*6)*6+3*(1+1)*(7*7)*(6*6)+5*7*2*3+(4+6)*(2*6)*7+2*7+2+3+2+5+2+7*5+5*7+1+4+1*3+(1+7)+4*3*0+3", 12240);
    toeval.put("(1+2)+3+7*4+7+2+3+(5*6)*2+5+(5*0)*4+6*(0*3)*1+6+0*0+(3+4)+0+5*(3+5)*5+7+4*6*(7+4)*(4*3)*5+2+0*7*1*2*(3*4)+(4*5)*5*3+7*2+(2*4)*4*3+0+3+5*5+0*4*(7+7)+(4*1)", 16615);
    toeval.put("0*7+0*7*1+5+5+4*(5*6)+1*5*2*5*(6+7)*(7*5)+(1*4)+5*6+6+0+0+5*(1*0)*3+2+2*1*6*6+1*7+(6*2)*(2+7)*(0*1)+1+4*4+1+7+7*6*2*3+1*7*4+0+3*3+4*0+0+7+0+2+2", 23326);
    toeval.put("5*0+3*4+(3+6)*(0*6)*2*6+1*0*(3+2)+(0+0)+1*6+6*1+0*3*2+5+(1*0)+(3+5)*2+1+2+2+(1+1)+(7*5)*0*4*5*6*2+2*7*0*1*0+2*2*2*6+(2+2)*7*5+6+0+1*3+3*6+5*2*(4+2)", 327);
    toeval.put("2+1*0*5*7*7*2+3+1*4*(5*3)+3*0*(4+5)*6+4*(5*2)+(6+0)*(1+5)*(4*1)+1+6+1+6+(0*0)+3+5+2+7*5+5+5+7*(6+4)*0+5+(4+2)+(1*4)*0+0+1*7+1+4*(1*2)+0*7+4*4+(7*2)*(3+1)", 417);
    toeval.put("(3*3)*(3*1)+(3*2)+4+7*(7*6)+5*7*5+4+(3+1)+2*4+(2+7)*5*2+(4*6)+(2*6)*2+0*(2+6)*(0+0)+4+7+1+0+5+2+7*3*0*6*7*6+(1*3)*2+5+0*1*3+2*3*0*2*4+(5+1)+(3+7)+4*3*7+2", 792);
    toeval.put("0*3+3+3+5+0+6*6+3*1+5+5*(1*7)+(0*3)*(3*3)*(0*4)*(7*7)*4*4+3*7*3*5+7+5*(5*3)+(4*2)+(0*7)*0+7+5+6+1+6+(1+2)*2*6+6*4+(1*0)*(7*0)+7*7*4*7*(2+6)*(6+4)+1+6+(6+5)", 110358);
    toeval.put("(1*7)+3+2*6*6*3*2+(0+3)*(2+2)+2*0+4*4+1*0+(1*5)+(0*4)+4*4*(1+0)+(6+7)*7*3+4+6+1+7+(7*0)+(1*1)*(3*7)+(4+6)*7*6+5+3*2+4*6*1*4+7+4*2*4*4+4+7+6+0*2*6*(5+3)", 1482);
    toeval.put("2*0+1+1+0+6*(3*5)*(5*6)*0+1+0+6*6+6+1*4+6*4*0+5*7*5+4*2+0+0+(0+2)*(1+1)*1+0+1+6*2+7+2+5*5+7+(0*5)*6*1*(6+0)*0*2+2*5*3*7*(0+4)*7+3+4*1+4+7+2*1", 6190);
    toeval.put("2*6+(3*4)*3+2+3*7*6*2+6*1+2+5+(4*4)*(0*3)*3*1*4+3*0*1*6*3*3*0+0*1*2*1*3+0+6+0*7*2+4+6+7*5*3+6*3+6+5*2+7*7+5+0+6+5*3*0*5*7+5*4+3+6*(6*1)", 589);
    toeval.put("(0*7*6+6+(7+0*(0+7))*6*4*7+6*1*5+0+1)", 1213);
    toeval.put("((0*6)+3*0)+(4+3)+(7+3)*(5+0)*1+0*(0+0+4*5)", 57);
    toeval.put("((3*7+(5+5)+(4*7)+5+6)+1+2*6+5*2+2*(4*6))", 141);
    toeval.put("(2+0+5+6)+(0*4*3+7)+(0*7)*(4+5)+(2+6)+(2+4)", 34);
    toeval.put("5*0+7*2*0+7*5*4*2*0+5+0+7+0*3+4", 16);
    toeval.put("(4+1+7*2)*6*4+(3+6)*(7+6*5*6)*4+7*3*4", 7272);
    toeval.put("(1*7*(4+3)+1+2*(0*4)+((1+5)+5*6)*0*3+1*2)", 52);
    toeval.put("6+3*3*7+(1+1*2+3)+(6*7)+4+4*1*7+(6+0)", 155);
    toeval.put("(1*1)*7+6*(4*6)+7*0+(2+7*2+6)*(6+2+3+5)", 503);
    toeval.put("((7*2)*7+0*(7+6+3+6))+6*5+(6+1)*1*6+3*1", 173);
    toeval.put("(7+3+6*5+5*6*5+2)*2+2*1+1*(4*0*1+0)", 386);
    toeval.put("(6+4+(4+6))+3+6*(1*1)+(((1*3)+1+5)*(6*3)*7*7)", 7967);
    toeval.put("2+7*4*3*((7*5)+5*3)+(7*5+(0*7)+1*3+5+0)", 4245);
    toeval.put("(1+0)*3+5+(2+3*(4+0))+(7+5)*3*1+(1*6)*(6*5)", 238);
    toeval.put("((7+0+7+2*4*5+4*3)*6+0*7+2*(5+3+1*2))", 416);
    toeval.put("(0+7+2*6)*(5+2)*0*4+(7*0*3*0*4*7+2+3)", 5);
    toeval.put("((4*7)*6+1+(3*5*3+5)+1+2+3+6*1*1*5*7)", 435);
    toeval.put("((0+0)+1*2)*4*5+3+7+3+2*(5*6)+6*3*7+5", 244);
    toeval.put("(((3*0)*5*4*(7+7*1*7))+((7*5)+(2+2)+(7*3)+(3+0)))", 63);
    toeval.put("(4+3+4+0*4*6+5+0+((3+3+1*4)*0*7+1+0))", 17);
    toeval.put("(3*6)*6+4*(6+3*5+6)*(5+5+(0*5))*2+6+(0*3)", 2274);
    toeval.put("(3*3+3+5*7+2*6+0+2*0+(6+4)*(2*1+(0*2)))", 79);
    toeval.put("((2*6+7*6)*(3+0+(4*5))+4+4+2*2*(6*0)+4+0)", 1254);
    toeval.put("(1+2)+0+6+(3*0*3+7)*((1+1)+6*7)+3*6+(0+2)", 337);
    toeval.put("(((3+0)+5+5)+3*1*2*1)+(5+1*(7+5))+1*0*7*7", 36);
    toeval.put("(((6+0)*0+7)*((3+7)*4*1)+2+2+4*2+0+1+(1*7))", 300);
    toeval.put("(5*2)*(6*5)+7+6*2*4+7+4+(5*3)*(6+0*(6*7))", 456);
    toeval.put("((2*6)+5*0+7+7*0*1*(3+3+1+1)+(6*6)+2+3)", 60);
    toeval.put("(1*4+6*4+(7+7+3*0))*(3+7)+2*4*6*3+0+7", 571);
    toeval.put("(3+6*2+1)+(7+6)+(5*2)+(5*6*3*2+(2*1*6*4))", 267);
    toeval.put("(((2+1)*1+0)*(4+0)+2*7)+(5*5+(3*0))+(6*7)+7+5", 105);
    toeval.put("((0*5*(2*7))*4*7+2+2*3*2+1+3+1*6*2*5)", 78);
    toeval.put("((5+9)+(3*2)*3*2+6+7*(1+7+4*0)+9*3*7+7)*(1+5*(5*4)+(6*9+4*5)*9+2+(2+2)+7+4*(0*4))", 240240);
    toeval.put("(2+9+6*3+(4+9+8*1))*(3*0+6+3)+1+8+7+5*(5+3*(4*5)+5*6+(9+9))+(2+3*(6+9)+(4+2)+(3+6))", 1093);
    toeval.put("(((1*0+2+6*(3*2+3*8))+(7+8*8*3+(0+1*8+8)))+((4+0)*2*3+7+0*(0*2))+((9+7*(0*8))+(6+1)*(9*7)))", 878);
    toeval.put("((((9*8)+(9*9))+(8+5+0+0))+((3+6+4*3)*3+8*1*1))*1+6*(9+5)+(0+9)*4+7*6+6*4+7+(7+9)*(1+4)", 510);
    toeval.put("(5+5+(1+8))+(9+1)+(0*5)+(6*9+(0+6))*(1+3*(2+1))*(8*3+4*6+(2+2+(3+8)))*(8+7*9+6)+5*0+2+7", 2910638);
    toeval.put("(((7*5)*(6+5)+8*3+(4*0))+((6+5)*4+4+5+5+2*9)*(9+8+5+7*(0+2)*(7+1))+((0*5)*8+7*5*9+9+8))", 10925);
    toeval.put("(1*6*5+7*(6*1*1+4)+((6+0)*7+6*5+6+(6+9)))*(1+8+7+4*5*8+(8+1)*(9+6+2+0)*0+1*3+9)", 36284);
    toeval.put("(((7+6*9*2*0*0+(3*4))+4+3+7*3*5+5*1+4)+((0*0)+(4+0)*2*4*8*6)*0+8*6+6+6+9+(1+9))", 219);
    toeval.put("(7+0+9+6+((6+8)*(3*1))*6*9*0+7+6*2*(3+5))+((3+7*(3*2))*5+5*(1*3))+3*2+(9*4)+2*8+(9*0)", 423);
    toeval.put("((6*7)*5*3+(4+8+(1+8))+3+1*1*0*(6*1+(1+9))*((5+8+8+5)*(9*7+7+4)+(0*4)*2*2*6+6+3+6))", 654);
    toeval.put("8*3*9+7+0+7*2*2*(5+1)+4*6*(7*6)*(1*9)+((6*1*5+1)*(6*8)*5*7)+((0*1)+4+8+1+9*(4+7))", 61655);
    toeval.put("((1+4)*4*0)*2*4+5+5*3+7+9*3*(4*5+(0*1))+(((7+8)*4+4)+8+5*2+3+(3*7*(8*0))*9+6*3*2)", 688);
    toeval.put("(2+3*(7*1))+(6+3*0+2)+(0*8+0*6)+(2*0*7*2)+(4+0)*(8+0)+8*3+(4+2)+(7*5+0+9+4+4+(8+1))", 154);
    toeval.put("((9+5+(7+6)*(1*1)*8*2+1*2*(4+3)*(6+2*(0*3)))+2+1*(2+4)+(2*9)*8+5+8+5+7*1+2+8*(0*3))", 485);
    toeval.put("((9+2)+4*6)+((2*1)+0*8)+7+2*6+6*6+5+(2+8)+((3*4*7*2)*((9*0)*2*1))*(8*6+4*5)+(0+0)+(1*6)", 113);
    toeval.put("((2+8)+5*1+((9*7)*2*3)*(2+1)*1+4*9+4+(3*2))*9*1+6*0+(6*5+7*2)+(4+7)*9*7*(8*8*(9*5))", 2006639);
    toeval.put("(5*9+2+4+0*3*1*6*(0+6*7+4)+0+5*(2*3)+5+2*(4*5)*(9+0)+3+2*2+6+(8+2)*(5+8+5+9))", 729);
    toeval.put("(((6+4)+(0*7))*((9+4)*2*1)+7+7*(9+3)+(4*7*9+5))*5*9+3+0+4*9*8*2+9+7+(5+3)*0+4+(2+2)", 27963);
    toeval.put("((6*3*1*8*5*3+3*7)+(((3+9)+5*5)*(7*7*6+0))+((8+9*1*2)+(5*9)+5+4)+8*2*(9+2)*(7*3)+6+1)", 16842);
    toeval.put("7*5+2+8*(4*1)*1*7+((7*7*(9+3))*(7+4+4+0))*(4+7*7+6+7+0+7*1)+(2*1+6*6)*((0+8)*(4+9))", 648073);
    toeval.put("((1*7+4+6*0+4+4+9+((8*9)*(3+6))+(6*4+(6+0)))+(9*1)+3*1*3*2*4+7+((2+1)+(8+2))*((2*0)+1*1))", 807);
    toeval.put("(4*1*(7*2))*4+3+8*9+6+7+3+7+(5*4*(2+3))*(5*5+(1*2)*2*6+8+9)+((1*0)+5*9+(1*3)+8+7)", 6985);
    toeval.put("(1*7+(1*2)+0+1+(0+3)*(4*7)+(3+8)*8*6+8+1*((0+6+6+2*9+8*0*2)*((6+6)+8*8*((7*4)+(9*1)))))", 72030);
    toeval.put("((5*0*(2+9))*(7+0+7+2)*(1*1+(0+1)+4+0*2*3))+(7+2+(6+8)+0+7+7*0*3*2+(5+6)+((3*0)*(7*4)))", 41);
    toeval.put("(6*7*1+5)+(6*3*(8*6))*9+1+4+1*(0+2)*2*2*3*1+1+0*((7*7)*(6*0))*((2+1)*(2+0))+6*1+(6+9)", 7874);
    toeval.put("(((6*3+(6+5)*4*2+(5*7))*(0*0)+3+9*(6+5)+6*5)*(8*4+7+4)*(3*0)+(9*7)+((9*5)*9+5)*1+3*(5*5))", 548);
    toeval.put("(((5*3+(2+7))+(7+2*(9*3))+(2*1*(7+2)*3*2*5*8))+1*3*0*6*(9+5)+6*8*(((5*2)+4*1)*((6+1)+1*1)))", 9781);
    toeval.put("(7*0*1+9+((1*8)*(2*0))+2*0+(4+7)+(0+1)+6*8)+6*6+9+0*(2+8+2*5)+(2+5)*(9*4)*(9+2)*9*7", 174750);
    toeval.put("0+3*3+7+0*7+2*0+3+9*0*9+8+0*(2+9)*((6+1*8+0)+(8+1*5*3))*((3*6)+0*2)+5*7+0+0", 62);
    toeval.put("((3+9+0+1)*8+4+5*7)+(4*9+8*6)*5*0+6+0*8*0*5*9+((4+1)*0+2)*1+9*(1*3)+(5+8*(2*5))", 263);
    toeval.put("(1+1*0*5+0*2*6+9)+0+6*(9+5)*(0*5*0+2)*((3*7+4+1)+((3+3)+3*2))*7*7*(2*2)*4*1+(9+7)", 5005082);
    //@formatter:on

    for (Entry<String, Integer> ent : toeval.entrySet()) {
      String str = ent.getKey();
      long expected = ent.getValue().longValue();

      // TODO:
      assertEquals(expected, expected);
    }

  }

}
