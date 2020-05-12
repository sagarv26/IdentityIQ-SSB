Ext.require([
    'Ext.form.field.File',
    'Ext.form.Panel',
    'Ext.window.MessageBox',
    'Ext.grid.Panel',
    'Ext.data.Store',
    'Ext.data.Connection'
]);

var msg = function(title, msg) {
           Ext.Msg.show({
            title: title,
            msg: msg,
            minWidth: 200,
            modal: true,
            icon: Ext.Msg.INFO,
            buttons: Ext.Msg.OK
        	});
    	};


var fileGridStore = Ext.create('Ext.data.Store', {
    storeId:'fileGrid',
    fields:['description', 'file', 'fileName', 'url', 'workItemId'],
    proxy: {
         type: 'ajax',
         url: SailPoint.getRelativeUrl('/external/FileUploadServlet'),
         actionMethods: { read: 'GET'
         },
         extraParams: {
            workItemId: this.workItemId,
            action: this.action
         },
         reader: {
             type: 'json',
             root: 'fileParameters'
         }
     },
     autoLoad: false
});


Ext.define('SailPoint.myFileUploadGrid', {
    extend :'Ext.grid.Panel',
    id : 'myFileUploadGrid', 
    store: fileGridStore,
    columns: [
        { text: 'Description',  dataIndex: 'description' },
	    { text: 'File name', dataIndex: 'fileName', flex: 1 },
        { text: 'View', dataIndex: 'url',
	  renderer: function(value) {
            return Ext.String.format('<a target="_blank" href="{0}">{1}</a>', value, 'View');
          }
	},
        { text: 'WorkItemId', dataIndex: 'workItemId', hidden: true },
	{ xtype:'actioncolumn',
            width:50,
            items: [{
                icon: SailPoint.getRelativeUrl('/scripts/ext-4.1.0/resources/images/tree/drop-no.gif'),
                tooltip: 'Delete',
                handler: function(grid, rowIndex, colIndex) {
		    var conn = new Ext.data.Connection();
		    conn.request({
			method : 'GET',
			url : SailPoint.getRelativeUrl('/external/FileUploadServlet'),
			params : {
			    workItemId : grid.getStore().getAt(rowIndex).get('workItemId'),
                            file : grid.getStore().getAt(rowIndex).get('file'),
			    action : 'update'
			},
			success : function(responseObject) {
			    var data = Ext.decode(responseObject.responseText);
                            Ext.getCmp('myFileUploadGrid').getStore().load();                         
			    msg('Success', 'Removed file "' + grid.getStore().getAt(rowIndex).get('fileName') + '".');

			}
		    });
                }
            }]
	}
    ],
    height: 110,
    constructor : function(config) {
        Ext.apply(this, config);
        this.callParent(arguments);
        this.store.proxy.extraParams = config.filters;
        this.store.load();
    },
    initComponent : function() {
        this.callParent(arguments);
    },
    renderTo: 'file-upload-div-grid'
});



Ext.define('SailPoint.myReadOnlyFileUploadGrid', {
    extend :'Ext.grid.Panel',
    id : 'myReadOnlyFileUploadGrid', 
    store: fileGridStore,
    columns: [
        { text: 'Description',  dataIndex: 'description' },
	    { text: 'FileName', dataIndex: 'fileName', flex: 1 },
        { text: 'View', dataIndex: 'url',
	  renderer: function(value) {
            return Ext.String.format('<a target="_blank" href="{0}">{1}</a>', value, 'View');
          }
	},
        { text: 'WorkItemId', dataIndex: 'workItemId', hidden: true },
    ],
    constructor : function(config) {
        Ext.apply(this, config);
        this.callParent(arguments);
        this.store.proxy.extraParams = config.filters;
        this.store.load();
    },
    initComponent : function() {
        this.callParent(arguments);
    },
    renderTo: 'file-upload-div-grid'
});



Ext.define('Sailpoint.mypanel', {
	config : {
		workItemId : null
	},
	extend : 'Ext.form.Panel',
	renderTo: 'file-upload-div-form',
        height: 130,
        frame: true,
        bodyPadding: '10 10 0',
       
	initComponent : function(config) {
		Ext.apply(this,config);
		this.items = [{
		    xtype: 'textfield',
		    fieldLabel: 'Description',
		    name: 'description'
		},{
		    xtype: 'filefield',
		    id: 'form-file',
		    emptyText: 'Select File',
		    fieldLabel: 'File',
		    name: 'file',
		    listeners     : {

			  change: function(fld, value) {
				if (value!=''){
				var newValue = value.replace(/C:\\fakepath\\/g, '');
				fld.setRawValue(newValue);
				}
			}
		  },
		    buttonText: 'Browse',
		    buttonConfig: {
			iconCls: 'upload-icon'
		}
		},{
		xtype: 'displayfield',
		value: '',
		id: 'downloadLink',
		name: 'downloadLink'
		},{ 
		xtype: 'hiddenfield',
		name: 'workItemId',
		id: 'workItemId',
		value: this.workItemId
	}],
	this.callParent(arguments);
	},

	defaults: {
            anchor: '100%',
            allowBlank: false,
            msgTarget: 'side',
            labelWidth: 80
        },

	
        buttons: [{
            text: 'Add File',
            handler: function(){
                var form = this.up('form').getForm();
                var workItemId = Ext.getCmp('workItemId').getValue();
                if(form.isValid()){
                    form.submit({
                        url: SailPoint.getRelativeUrl('/external/FileUploadServlet?workItemId=') + workItemId,
                        waitMsg: 'Uploading your File...',
                        success: function(form, action) {
                        response = Ext.decode(action.response.responseText);
                        if(response.success == true){
			        Ext.getCmp('myFileUploadGrid').getStore().load();                         
				msg('Success', 'Uploaded file "' + response.fileName + '".');
			}else{
				msg('Error', response.errorMessage);
			}
                        },
                        failure: function(form, action) {
                        response = Ext.decode(action.response.responseText);
			msg('Error', response.errorMessage);
                        }
                    });
                }
            }
        },{
            text: 'Reset',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }]
});