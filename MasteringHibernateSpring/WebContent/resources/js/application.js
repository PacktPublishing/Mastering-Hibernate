$(document).ready(function() {

	hideDiv('personAddUpdateModal');
	hideNotices();

	var personDataTable = $('#personTable').DataTable({
		'ajax': {url:'/MHS/rest/person/list', dataSrc: 'data'},
		'columns': [
		          { data: 'id' },
		          { data: 'firstname' },
		          { data: 'lastname' },
		          { data: 'ssn' },
		          { data: 'birthdate' }
		      ],
		'columnDefs': [{
				'render': function(data, type, row) {
						return '<a href="#" id="personId_' + data +'" class="personIdLink">' + data + '</a>';
				},
				'targets': 0
			}, {
				'render': function(data, type, row) {
					return '<a href="#" id="personDelete_' + row.id +'" class="personDeleteLink"><span class="ui-icon ui-icon-trash"></span></a>';
			},
			'targets': 5
			}
			
			],
		      
	});


	$('#personAddUpdateModal').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		close: function() {
	        $('#addUpdateForm')[0].reset();
		}
	});

	// late binding for id links
	$( document ).on('click', 'a.personIdLink', function(event) {
		var personId = $(this).html();
		$.ajax({
			url: '/MHS/rest/person/view/' + personId,
			dataType: 'json',
			success: function(data, status, xhr) {
				populateForm(data);
				// make sure the button reads "Update"
				$('#updatePersonButton').val('Update');
			},
			error: function(xhr, textStatus, errorThrown ) {
				reportError(textStatus + '<p/>' + errorThrown);
				console.log(textStatus + "\n" + errorThrown);
			}
		});
		$('#personAddUpdateModal').dialog("open");
	});
	
	// late binding for delete links
	$( document ).on('click', 'a.personDeleteLink', function(event) {
		var thisId = $(this).attr('id');
		
		var personId = thisId.split('_')[1];
		console.log('deleting: ' + personId);
		$.ajax({
			url: '/MHS/rest/person/delete/' + personId,
			type: 'GET',
			success: function(data, status, xhr) {
				refreshDataTable();
				reportSuccess('successfully deleted the person');
			},
			error: function(xhr, textStatus, errorThrown ) {
				reportError(textStatus + '<p/>' + errorThrown);
				console.log(textStatus + "\n" + errorThrown);
			},
		});
		refreshDataTable();
	});
	
	$( document ).on('click', '.closeAlert', function(event) {
		$(this).parent().hide();
	});

	
	$('#addPersonLink').click(function() {
		console.log('add person link click');
		$('#addUpdateForm').find('#personId').html('');
		$('#updatePersonButton').val('Add Person');
		$('#personAddUpdateModal').dialog("open");
	});

	$('#cancelUpdateButton').click(function() {
		$('#personAddUpdateModal').dialog('close');	
	});
	
	$('#updatePersonButton').click(function() {
		// collect form data
		var person = {};
		person.id = $('#addUpdateForm').find('#personId').html();
		person.firstname = $('#addUpdateForm').find('input[name="firstname"]').val();
		person.lastname = $('#addUpdateForm').find('input[name="lastname"]').val();
		person.ssn = $('#addUpdateForm').find('input[name="ssn"]').val();
		person.birthdate = $('#addUpdateForm').find('input[name="birthdate"]').val();
		
		if ($('#updatePersonButton').val() == 'Update') {
			console.log('this is an update');
			$.ajax({
				type: 'POST',
				url: '/MHS/rest/person/update',
				data: JSON.stringify(person),
				contentType: 'application/json',
				success: function(data, status, xhr) {
					refreshDataTable();
					reportSuccess('update successful.');
					console.log('posted successfully');
				},
				error: function(xhr, textStatus, errorThrown ) {
					reportError(textStatus + '<p/>' + errorThrown);
					console.log(textStatus + "\n" + errorThrown);
				}
			});
		}
		else {
			console.log('this is an add');
			$.ajax({
				type: 'POST',
				url: '/MHS/rest/person/add',
				data: JSON.stringify(person),
				contentType: 'application/json',
				success: function(data, status, xhr) {
					refreshDataTable();
					reportSuccess('successfully added the person');
					console.log('posted successfully');
				},
				error: function(xhr, textStatus, errorThrown ) {
					reportError(textStatus + '<p/>' + errorThrown);
					console.log(textStatus + "\n" + errorThrown);
				}
			});			
		}
		$('#personAddUpdateModal').dialog('close');
	});
		
	function populateForm(data) {
		$('#addUpdateForm').find('#personId').html(data.id);
		$('#addUpdateForm').find('input[name="firstname"]').val(data.firstname);
		$('#addUpdateForm').find('input[name="lastname"]').val(data.lastname);
		$('#addUpdateForm').find('input[name="ssn"]').val(data.ssn);
		$('#addUpdateForm').find('input[name="birthdate"]').val(data.birthdate);
	}
	
	function toggleDiv(divId) {
		$('#' + divId).toggle('slow');
	}

	function hideDiv(divId) {
		$('#' + divId).hide();
	}
	
	function refreshDataTable() {
		// personDataTable.ajax.url('/MHS/rest/person/list').load();
		personDataTable.ajax.reload(null, true);
	}
	
	function hideNotices() {
		$(".alert").hide();
	}
	
	function reportSuccess(message) {
		hideNotices();
		$(".alert-success").show();
		$("#successMessage").html(message);
	}
	
	function reportError(message) {
		hideNotices();
		$(".alert-danger").show();
		$("#errorMessage").html(message);
	}
	
});
