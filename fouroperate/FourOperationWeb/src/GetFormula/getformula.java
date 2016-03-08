package GetFormula;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class getformula
 */
@WebServlet("/getformula")
public class getformula extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public getformula() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int a,b,c,d,index,p1,p2;
		int temp;
		double sum;
		String tempLen;
		String punctuation[] = {"+","-","*","/"};
		String formula[] = new String[1000];
		String checkvalue1 = request.getParameter("num1"); //接收用户输入的四个数字
		String checkvalue2 = request.getParameter("num2");
		String checkvalue3 = request.getParameter("num3");
		String checkvalue4 = request.getParameter("num4");
		
		//用户输入的顺序即为公式中数字的顺序
		formula[0] = checkvalue1; 
		formula[2] = checkvalue2;
		formula[4] = checkvalue3;
		formula[6] = checkvalue4;
		
		// 生成公式
		for(int i = 1;i < 6;i+=2){ 
			index = (int) Math.floor(Math.random()*4);
			formula[i] = punctuation[index];
		}
		
		//生成插入括号的位置
		p1 = (int) Math.floor(Math.random()*4*2);
		p2 = (int) Math.floor(Math.random()*4*2);
		while(Math.abs(p1-p2)==1||p1==p2){
			p1 = (int) Math.floor(Math.random()*4*2);
			p2 = (int) Math.floor(Math.random()*4*2);
		}
		if(p1 > p2){
			temp =p1;
			p1 = p2;
			p2 = temp;
		}
		
		//插入括号
		if(p1%2==0){  
			for(int i = 6;i >=p1;i--){
				formula[i+1] = formula[i];
			}
			formula[p1] = "(";
		}else{
			for(int i = 6;i >=p1-1;i--){
				formula[i+1] = formula[i];
			}
			formula[p1-1] = "(";
		}
		if((p2+1)%2==0){
			for(int i = 7;i >= p2+1;i--){
				formula[i+1] = formula[i];
			}
			formula[p2+1] = ")";
		}else{
			for(int i = 7;i >= p2+2;i--){
				formula[i+1] = formula[i];
			}
			formula[p2+2] = ")";
		}
		
		//结果字符串
		tempLen = (String) calFormula(formula);
		int len = 0;
		int flag = 0;
		for(int i=0;i < tempLen.length();i++){
			char item = tempLen.charAt(i);
			if(item =='.') flag = 1;
			else if(flag == 1){
				len++;
			}
			
		}
		double out = Double.parseDouble(tempLen);
		//保留小数点后两位
		if(len>2) out = ((int)(out*100))/100;
		System.out.println(out);

		//传给jsp页面
		String forward = null;
		request.setAttribute("result1",formula);
		request.setAttribute("result2",out);
		ServletContext sc=this.getServletContext();
		forward="/result.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}
	
	// 计算结果
	public Object calFormula(String formula[]){
		String test,opeTop,tempTop;
		String tempFor[] = new String[10];
		int prio1,prio2;
		double cal = 0;
		Stack ope = new Stack();
		Stack num = new Stack();
		Stack fin = new Stack();
		for(int i = 0;i <= 8;i++){
			if(isOperation(formula[i])){
				if(ope.isEmpty()){
					ope.push(formula[i]);
				}else{
					opeTop = (String) ope.pop();
					ope.push(opeTop);
					if(opeTop=="("){
						ope.push(formula[i]);
					}else{
						prio1 = getPriorty(opeTop);
						prio2 = getPriorty(formula[i]);
						if(prio1 < prio2){
							ope.push(formula[i]);
						}else{
							tempTop = (String) ope.pop();
							num.push(tempTop);
							i--;
						}
					}
				}
			}else if(formula[i]=="("){
				ope.push(formula[i]);
			}else if(formula[i]==")"){
				tempTop = (String) ope.pop();
				while(tempTop != "("){
					num.push(tempTop);
					tempTop = (String) ope.pop();
					//System.out.println(tempTop);
				}
			}else if(!isOperation(formula[i])){
				num.push(formula[i]);
				
			}
		}
		
		while(ope.isEmpty()!=true){
			tempTop = (String) ope.pop();
			num.push(tempTop);
		}
		
		int j = num.size()-1;
		int count = num.size();
		for(int i=0;i < count;i++){
			tempFor[j] = (String)num.pop();
			j--;
		}
		
		for(int i=0;i<count;i++){
			if(tempFor[i]=="+"||tempFor[i]=="-"||tempFor[i]=="*"||tempFor[i]=="/"){
				String p1 = (String) fin.pop();
				String p2 = (String) fin.pop();
				double a = Double.parseDouble(p1);
				double b = Double.parseDouble(p2);
				switch(tempFor[i]){
					case "+":cal = b+a;break;
					case "-":cal = b-a;break;
					case "*":cal = b*a;break;
					case "/":cal = b/a;break;
				}
				fin.push(String.valueOf(cal));
			}else{
				fin.push(tempFor[i]);
			}
		}
		return fin.pop();
	}
	
	//判断是否为运算符
	public boolean isOperation(String str){
		if(str=="+" || str=="-" || str=="*" || str=="/")
				return true;
		else
			return false;
		
	}
	
	//获取优先级
	public int getPriorty(String str){
		if(str=="+" || str=="-")
			return 0;
		else
			return 1;
		
	}
}



