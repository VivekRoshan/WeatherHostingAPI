function forecast()
{
$("#forecast").ready(function() {
        //$("div#myOutput").html(" ");
        
        var date1 = document.getElementById('datevalue').value;
	 	console.log(date1);	 	 	
	 	var datearr = []
        var tmaxarr = [];
        var tminarr = []
        var returnDate= "";
        returnData = "<center><table style='font-size:12pt;'><tr><th>DATE</th><th>TMAX</th><th>TMIN</th></tr>";
        var formattedDate = new Date(date1);
        if(!(formattedDate instanceof Date && !isNaN(formattedDate.valueOf())))
    	{
    	
    	 $("div#myOutput").html("Enter a valid Date");
    	return(false);
    	}
        if(!(formattedDate.getFullYear() < 2020 ))
    	{
    	 $("div#myOutput").html("Enter a valid Date before 2020");
     	return(false);
    	}
    
    
        for(var i=0;i<7;i++)
	 	{
	 	date = new Date(date1);
	 	date.setDate(date.getDate()+1-i);
	 	console.log(date);
	 	var d = date.getDate();
	 	var m =  date.getMonth();
	 	m += 1;  // JavaScript months are 0-11
	 	var y = date.getFullYear();
	 	date = y + "-" + (m>9 ? '' : '0') + m + "-" + (d>9 ? '' : '0') + d;
	 	console.log(date);
	 	datearr.push(date);

	 	var city = document.getElementById('city').value;
	 	var result = []
	 	 var apiurl = "http://apidev.accuweather.com/locations/v1/search?q="+city+"&apikey=hoArfRosT1215";
  	 	 $.ajaxSetup({
  	         async: false
  	     });

  	        $.ajax({
  	            type: "GET",
  	            dataType: "json",
  	            async: false,
  	            url: apiurl,
  	            error: function(request, status, error) { alert(request.responseText) },
  	            success: function(data) {
  	            console.log(data);
  				result[0]=data[0].GeoPosition.Latitude;
  				result[1]=data[0].GeoPosition.Longitude;
  				//result.push(data[0].GeoPosition.Latitude);
  				
  				console.log(result[0]);
  				console.log(result[1]);
  				
  				
  	         }
           }); 
  	       

	 	
	 	
	 	
	 	var apiurl = "https://api.darksky.net/forecast/3476ac5af82da1f43aa6e9b8fd29d0b4/"+result[0]+","+result[1]+","+date+"T12:00:00";
	 	 
	 	 $.ajaxSetup({
	         async: false
	     });

	        $.ajax({
	            type: "GET",
	            dataType: "json",
	            async: false,
	            url: apiurl,
	            error: function(request, status, error) { alert(request.responseText) },
	            success: function(data) {

	 			    //console.log(data); 
	 			   //datearr.push(date);
			 returnData += "<tr><td>" +	date + "</td><td align='right'>"
	 			+ " " + data.daily.data["0"].temperatureMax + "</td><td align='right'>"
	 			+ data.daily.data["0"].temperatureMin + "</td></tr>";
	 	

	 			    tminarr.push(data.daily.data["0"].temperatureMin);
	 			   tmaxarr.push(data.daily.data["0"].temperatureMax);
	            }
	            }); 
		 	 $.ajaxSetup({
		         async: true
		     });

	 	
	 	}

		 returnData = returnData + "</table></center>";

        console.log(tmaxarr);
		console.log(tminarr);
		console.log(datearr);
		
		var chart = new CanvasJS.Chart("chartContainer2",
			    {
			      title:{
			      text: "Weather Hosting Bonus"  
			      },
			      animationEnabled: true,
			      toolTip:{
                      shared:true
                    },
			      theme: "theme2",
			      legend:{
						verticalAlign: "center",
						horizontalAlign: "right"
					},
			      data: [
			      {        
			        type: "line",
			        showInLegend: true,
					lineThickness: 2,
					name: "TMIN",
					markerType: "square",
					color: "#F08080",
			        dataPoints: [
			        { x: new Date(datearr[0]), y: tminarr[0] },
			        { x: new Date(datearr[1]), y: tminarr[1] },
			        { x: new Date(datearr[2]), y: tminarr[2] },
			        { x: new Date(datearr[3]), y: tminarr[3] },
			        { x: new Date(datearr[4]), y: tminarr[4] },
			        { x: new Date(datearr[5]), y: tminarr[5] },
			        { x: new Date(datearr[6]), y: tminarr[6] },
			        
			      
			        ]
			      },
			        {        
			        type: "line",
			        showInLegend: true,
					name: "TMAX",
					color: "#20B2AA",
					lineThickness: 2,
			        dataPoints: [
			        { x: new Date(datearr[0]), y: tmaxarr[0] },
			        { x: new Date(datearr[1]), y: tmaxarr[1] },
			        { x: new Date(datearr[2]), y: tmaxarr[2] },
			        { x: new Date(datearr[3]), y: tmaxarr[3] },
			        { x: new Date(datearr[4]), y: tmaxarr[4] },
			        { x: new Date(datearr[5]), y: tmaxarr[5] },
			        { x: new Date(datearr[6]), y: tmaxarr[6] },
			        
			      
			        ]
			      },        
			      ],
		          legend:{
		              cursor:"pointer",
		              itemclick:function(e){
		                if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
		                	e.dataSeries.visible = false;
		                }
		                else{
		                  e.dataSeries.visible = true;
		                }
		                $("div#bonusmyOutput").html(chart.render());

		              }
		          }
			    });

		$("div#bonusmyOutput").html(chart.render());
		$("div#bonusmyOutput").html(returnData);
	 	return (false);
});

}