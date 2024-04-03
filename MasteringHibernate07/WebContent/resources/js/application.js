$(document).ready(function() {

	hideDiv('personAddUpdateModal');
	hideDiv('search');

	var personDataTable = $('#personTable').DataTable({
		'ajax': {url:'/MH/person', dataSrc: 'data'},
		'columns': [
		          { data: 'id' },
		          { data: 'firstname' },
		          { data: 'lastname' },
		          { data: 'ssn' },
		          { data: 'birthdate' },
		          { data: 'childCount'}
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
			'targets': 6
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
			url: '/MH/person/' + personId,
			dataType: 'json',
			success: function(data, status, xhr) {
				populateForm(data);
				// make sure the button reads "Update"
				$('#updatePersonButton').val('Update');
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
			url: '/MH/person/delete/' + personId,
			type: 'POST',
			dataType: 'json',
			success: function(data, status, xhr) {
				refreshDataTable();
			}
		});
		refreshDataTable();
	});
	
	$('#searchPersonLink').click(function() {
		toggleDiv('search');
	});

	$('#addPersonLink').click(function() {
		console.log('add person link click');
		hideDiv('search');
		$('#updatePersonButton').val('Add Person');
		$('#personAddUpdateModal').dialog("open");
	});

	$('#cancelSearch').click(function() {
		hideDiv('search');
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
				url: '/MH/person/update/' + person.id,
				data: JSON.stringify(person),
				dataType: 'json',
				contentType: 'application/json',
				success: function(data, status, xhr) {
					console.log('posted successfully');
				}
			});
		}
		else {
			console.log('this is an add');
			$.ajax({
				type: 'POST',
				url: '/MH/person/add',
				data: JSON.stringify(person),
				dataType: 'json',
				contentType: 'application/json',
				success: function(data, status, xhr) {
					console.log('posted successfully');
				}
			});			
		}
		$('#personAddUpdateModal').dialog('close');
		refreshDataTable();
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
		personDataTable.ajax.url('/MH/person').load();
	}		
});
