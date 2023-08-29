console.log("hello js...");

const toggleSidebar = ()=>{
 
 if($(".sidebar").is(":visible")){
    document.getElementById("mySidebar").style.display = "none";
  document.getElementById("mySidebar").classList.add("w3-animate-opacity");
    $(".content1").css("margin-left","4%");
     $(".content2").css("margin-left","0%");
}
 else{
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("mySidebar").classList.add("w3-animate-left");
    $(".content1").css("margin-left","20%");
     $(".content2").css("margin-left","15%");
    $(".content2").css("background-size","cover");
 }
};

function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
     $(".content1").css("margin-left","4%");
}

/*search*/
const search = () => {
     console.log("searching");
    const query = $("#search-input").val();
    if (query == '') {
        $(".search-result").hide();
    } else {
        const url = `http://localhost:8282/search/${query}`;
        
        fetch(url)
            .then((response) => 
               {
                    return response.json();
               })
            .then((data) => {
               console.log(data);

               // If you want to dynamically construct HTML for search results
                let text = `<div class="list-group">`;
                data.forEach((d) => {
                    text += `<a href='/user/${d.cId}/contact-details' class='list-group-item list-group-action'>${d.name}</a>`;
                });
                text += `</div>`;

                $(".search-result").html(text);

                $(".search-result").show();
            })
            .catch(error => {
                console.error("An error occurred:", error);
            });
  }
};

/** payment gatepay javascript code  */

// first request to server to create order 

const paymentStart = ()=>{
    //payment has started
    console.log("Payment started ...");

    //fetching the amount
    let amount = $("#amount").val();

    //check if amt is blank oconsole.log
    if(amount=="" || amount==null){
       // alert("Amount is required !!");
        swal("Failed", "Amount is required !!", "error");
        return;
    }
   
        console.log(amount);
        // code , creating order request to the server using ajax
        $.ajax({

            url:'/user/create_order',
            data:JSON.stringify({amt:amount , info : "order_request"}),
            contentType : 'application/json',
            type : 'POST',
            dataType:'JSON',
            success : function(response){
                // invoke when success happened ,   create_order is created
                console.log(response);
                if(response.status == "created"){
                    // open payment form
                    var options = {
                        "key": "rzp_test_lm6jr3XNPbAvBS", // Enter the Key ID generated from the Dashboard
                        "amount": response.amount, // Amount is in currency subunits. Default currency is
                       // INR. Hence, 50000 refers to 50000 paise
                        "currency": "INR",
                        "name": "Smart Contact Manager",
                        "description": "Donation",
                        "image": "/images/sunflower.jpg",
                        "order_id": response.id, //This is a sample Order ID. Pass the
                       // `id` obtained in the response of Step 1
                        "handler": function (response){
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature)
                        console.log("Payment successfull");

                        updatePaymentOnServer(
                            response.razorpay_payment_id,
                            response.razorpay_order_id,
                            "Paid"
                        );

                        //alert("Congrats ! payment is successfull.");
                        
                        },
                        "prefill": {
                        "name": "Gaurav Kumar",
                        "email": "gaurav.kumar@example.com",
                        "contact": "9999999999"
                        },
                        "notes": {
                        "address": "Learn code with durgesh"
                        
                        },
                        "theme": {
                        "color": "#3399cc"
                        }
                        };
                        // initiate payment 
                        var rzp1 = new Razorpay(options);
                        rzp1.on('payment.failed', function (response){
                        console.log(response.error.code);
                        console.log(response.error.description);
                        console.log(response.error.source);
                        console.log(response.error.step);
                        console.log(response.error.reason);
                        console.log(response.error.metadata.order_id);
                        console.log(response.error.metadata.payment_id);
                        //alert("oops ! Payment failed .");
                        swal("Failed", "oops ! Payment failed .", "error");
                        });
                       // document.getElementById('rzp-button1').onclick = function(e){}
                        rzp1.open();
                       // e.preventDefault();
                        
                }
            },
            error : function(error){
                // gives error if create_order is not successfull
                console.log(error);
                //alert("something went wrong....");
                swal("Failed", "something went wrong.....", "error");

            }

        });
    };

    // sending successfull payment status to server and db
    function updatePaymentOnServer(payment_id,order_id,statuss){
           $.ajax({
            url:'/user/update_order',
            data:JSON.stringify({payment_id:payment_id , order_id : order_id , statuss : statuss}),
            contentType : 'application/json',
            type : 'POST',
            dataType:'JSON',
            success:function(response){
                swal("Good job!", "Congrats ! payment is successfull.", "success");
            },
            error : function(error){
                swal("Failed", "oops ! Your Payment is successfull , but we can't get on server , we will contact you as soon as possible !.", "error");
            }


           });

    }